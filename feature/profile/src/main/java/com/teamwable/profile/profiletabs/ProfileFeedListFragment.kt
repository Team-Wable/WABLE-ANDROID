package com.teamwable.profile.profiletabs

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import com.teamwable.common.util.AmplitudeProfileTag.CLICK_WRITE_FIRSTPOST
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileFeedBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.getSerializableCompat
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.shareAdapter.FeedViewHolder
import com.teamwable.ui.shareAdapter.PagingLoadingAdapter
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg.FEED_ID
import com.teamwable.ui.util.BundleKey
import com.teamwable.ui.util.FeedActionHandler
import com.teamwable.ui.util.FeedTransformer
import com.teamwable.ui.util.Navigation
import com.teamwable.ui.util.SingleEventHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFeedListFragment : BindingFragment<FragmentProfileFeedBinding>(FragmentProfileFeedBinding::inflate) {
    private val viewModel: ProfileFeedListViewModel by viewModels()
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private lateinit var userType: ProfileUserType
    private lateinit var userNickname: String
    private var userId: Long = -1
    private lateinit var feedActionHandler: FeedActionHandler
    private val singleEventHandler: SingleEventHandler by lazy { SingleEventHandler.from() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userType = setUserType()
        userNickname = arguments?.getString(BundleKey.USER_NICKNAME).orEmpty()
        userId = arguments?.getLong(BundleKey.USER_ID) ?: -1
    }

    private fun setUserType() = arguments?.getSerializableCompat(BundleKey.USER_TYPE, ProfileUserType::class.java) ?: ProfileUserType.EMPTY

    override fun initView() {
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), requireParentFragment().parentFragmentManager, viewLifecycleOwner)
        setAdapter()
        collect()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is ProfileFeedUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    else -> Unit
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is ProfileFeedSideEffect.DismissBottomSheet -> findNavController().popBackStack()
                    is ProfileFeedSideEffect.ShowSnackBar -> parentFragment?.let { Snackbar.make(it.view ?: return@let, sideEffect.type).show() }
                }
            }
        }
    }

    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.HomeDetail, mapOf(FEED_ID to feed.feedId))
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

        override fun onPostAuthorProfileClick(id: Long) {}

        override fun onFeedImageClick(image: String) {
            feedActionHandler.onImageClick(image)
        }

        override fun onKebabBtnClick(feed: Feed) {
            feedActionHandler.onKebabBtnClick(
                feed,
                fetchUserType = { userType },
                removeFeed = { viewModel.removeFeed(it) },
                reportUser = { nickname, content -> viewModel.reportUser(nickname, content) },
            )
        }

        override fun onCommentBtnClick(feedId: Long) {}
    }

    private fun setAdapter() {
        binding.rvProfileFeed.apply {
            adapter = feedAdapter.withLoadStateFooter(PagingLoadingAdapter())
            if (itemDecorationCount == 0) setDivider(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
        }
        submitList()
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewModel.updateFeeds(userId).collectLatest { pagingData ->
                val transformedPagingData = pagingData.map {
                    val transformedFeed = FeedTransformer.handleFeedsData(it, binding.root.context)
                    val isAuth = userType == ProfileUserType.AUTH
                    transformedFeed.copy(isAuth = isAuth)
                }
                feedAdapter.submitData(transformedPagingData)
            }
        }

        viewLifeCycleScope.launch {
            feedAdapter.loadStateFlow.collectLatest { loadStates ->
                val isEmptyList = loadStates.refresh is LoadState.NotLoading && feedAdapter.itemCount == 0
                setEmptyView(isEmptyList)
            }
        }
    }

    private fun setEmptyView(isEmpty: Boolean) = with(binding) {
        when (userType) {
            ProfileUserType.AUTH -> {
                tvProfileFeedAuthEmptyLabel.text = getString(R.string.label_profile_feed_auth_empty, userNickname)
                groupAuthEmpty.visible(isEmpty)
                initNavigateToPostingBtnClickListener()
            }

            ProfileUserType.MEMBER -> {
                tvProfileFeedMemberEmpty.text = getString(R.string.label_profile_feed_member_empty, userNickname)
                tvProfileFeedMemberEmpty.visible(isEmpty)
            }

            ProfileUserType.EMPTY -> return
        }
    }

    private fun initNavigateToPostingBtnClickListener() {
        binding.btnProfileFeedAuthNavigatePosting.setOnClickListener {
            trackEvent(CLICK_WRITE_FIRSTPOST)
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Posting)
        }
    }

    companion object {
        fun newInstance(userId: Long, nickName: String, type: ProfileUserType): ProfileFeedListFragment {
            return ProfileFeedListFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleKey.USER_ID, userId)
                    putString(BundleKey.USER_NICKNAME, nickName)
                    putSerializable(BundleKey.USER_TYPE, type)
                }
            }
        }
    }
}
