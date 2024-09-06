package com.teamwable.home

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.teamwable.home.databinding.FragmentHomeBinding
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.model.LikeState
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.parcelable
import com.teamwable.ui.extensions.setDividerWithPadding
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.shareAdapter.FeedViewHolder
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.BundleKey.FEED_STATE
import com.teamwable.ui.util.BundleKey.HOME_DETAIL_RESULT
import com.teamwable.ui.util.BundleKey.IS_FEED_REMOVED
import com.teamwable.ui.util.BundleKey.IS_UPLOADED
import com.teamwable.ui.util.BundleKey.POSTING_RESULT
import com.teamwable.ui.util.FeedActionHandler
import com.teamwable.ui.util.FeedTransformer
import com.teamwable.ui.util.Navigation
import com.teamwable.ui.util.SingleEventHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private lateinit var feedActionHandler: FeedActionHandler
    private val singleEventHandler: SingleEventHandler by lazy { SingleEventHandler.from() }

    override fun initView() {
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        collect()
        setAdapter()
        initNavigatePostingFabClickListener()
        fetchFeedUploaded()
        fetchFeedFromHomeDetail()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is HomeUiState.Loading -> findNavController().navigate(HomeFragmentDirections.actionHomeToLoading())
                    is HomeUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    is HomeUiState.Success -> Unit
                    is HomeUiState.AddPushAlarmPermission -> if (uiState.isAllowed == null) initPushAlarmPermissionAlert()
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is HomeSideEffect.ShowSnackBar -> Snackbar.make(binding.root, sideEffect.type).show()
                    is HomeSideEffect.DismissBottomSheet -> findNavController().popBackStack()
                    else -> Unit
                }
            }
        }
    }

    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            findNavController().navigate(HomeFragmentDirections.actionHomeToHomeDetail(feed.feedId))
        }

        override fun onGhostBtnClick(postAuthorId: Long, feedId: Long) {
            feedActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateGhost(Ghost(stringOf(AlarmTriggerType.CONTENT.type), postAuthorId, feedId))
            }
        }

        override fun onLikeBtnClick(viewHolder: FeedViewHolder, id: Long, isLiked: Boolean) {
            feedActionHandler.onLikeBtnClick(viewHolder, id) { feedId, likeState ->
                singleEventHandler.debounce(coroutineScope = lifecycleScope) {
                    if (isLiked != viewHolder.likeBtn.isChecked) viewModel.updateLike(feedId, likeState)
                }
            }
        }

        override fun onPostAuthorProfileClick(id: Long) {
            handleProfileNavigation(id)
        }

        override fun onFeedImageClick(image: String) {
            feedActionHandler.onImageClick(image)
        }

        override fun onKebabBtnClick(feed: Feed) {
            feedActionHandler.onKebabBtnClick(
                feed,
                fetchUserType = { viewModel.fetchUserType(it) },
                removeFeed = { viewModel.removeFeed(it) },
                reportUser = { nickname, content -> viewModel.reportUser(nickname, content) },
            )
        }

        override fun onCommentBtnClick(feedId: Long) {}
    }

    private fun handleProfileNavigation(id: Long) {
        when (viewModel.fetchUserType(id)) {
            ProfileUserType.AUTH -> (activity as Navigation).navigateToProfileAuthFragment()
            ProfileUserType.MEMBER -> findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to id))
            ProfileUserType.EMPTY -> return
        }
    }

    private fun setAdapter() {
        binding.rvHome.apply {
            adapter = feedAdapter
            if (itemDecorationCount == 0) setDividerWithPadding(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
        }
        submitList()
        scrollToTopOnRefresh()
        setSwipeLayout()
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewModel.updateFeeds().collectLatest { pagingData ->
                val transformedPagingData = pagingData.map {
                    val transformedFeed = FeedTransformer.handleFeedsData(it, binding.root.context)
                    val isAuth = viewModel.fetchUserType(transformedFeed.postAuthorId) == ProfileUserType.AUTH
                    transformedFeed.copy(isAuth = isAuth)
                }
                feedAdapter.submitData(transformedPagingData)
            }
        }
    }

    private fun scrollToTopOnRefresh() {
        var isFirstLoad = true

        viewLifeCycleScope.launch {
            feedAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.source.refresh is LoadState.Loading) isFirstLoad = false

                if (loadStates.source.refresh is LoadState.NotLoading && !isFirstLoad) {
                    binding.rvHome.scrollToPosition(0)
                    isFirstLoad = true
                }
            }
        }
    }

    private fun setSwipeLayout() {
        binding.layoutHomeSwipe.setOnRefreshListener {
            binding.layoutHomeSwipe.isRefreshing = false
            feedAdapter.refresh()
        }
    }

    private fun initNavigatePostingFabClickListener() {
        binding.fabHomeNavigatePosting.setOnClickListener {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Posting)
        }
    }

    private fun fetchFeedUploaded() {
        parentFragmentManager.setFragmentResultListener(POSTING_RESULT, viewLifecycleOwner) { _, result ->
            val isUploaded = result.getBoolean(IS_UPLOADED, false)
            if (isUploaded) feedAdapter.refresh()
        }
    }

    private fun fetchFeedFromHomeDetail() {
        parentFragmentManager.setFragmentResultListener(HOME_DETAIL_RESULT, viewLifecycleOwner) { _, result ->
            val feed: Feed = result.parcelable(FEED_STATE) ?: return@setFragmentResultListener
            val isRemoved = result.getBoolean(IS_FEED_REMOVED)
            if (isRemoved) viewModel.updateFeedRemoveState(feed.feedId)
            viewModel.updateFeedGhostState(feed.postAuthorId, feed.isPostAuthorGhost)
            viewModel.updateFeedLikeState(feed.feedId, LikeState(feed.isLiked, feed.likedNumber))
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        when (it) {
            true -> handlePushAlarmPermissionGranted()
            false -> handlePushAlarmPermissionDenied()
        }
    }

    private fun initPushAlarmPermissionAlert() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val permissionList = android.Manifest.permission.POST_NOTIFICATIONS
//            requestPermission.launch(permissionList)
//        } else {
//            handlePushAlarmPermissionGranted()
//        }
    }

    private fun handlePushAlarmPermissionGranted() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.patchUserProfileUri(
                        MemberInfoEditModel(
                            isPushAlarmAllowed = true,
                            fcmToken = task.result,
                        ),
                    )
                    Timber.tag("fcm").d("fcm token: $task.result")
                } else {
                    Timber.d(task.exception)
                    return@OnCompleteListener
                }
            },
        )
    }

    private fun handlePushAlarmPermissionDenied() =
        viewModel.patchUserProfileUri(MemberInfoEditModel(isPushAlarmAllowed = false))
}
