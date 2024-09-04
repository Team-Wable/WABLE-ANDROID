package com.teamwable.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import com.teamwable.home.databinding.FragmentHomeBinding
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.setDividerWithPadding
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.FeedActionHandler
import com.teamwable.ui.util.FeedTransformer
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private lateinit var feedActionHandler: FeedActionHandler

    override fun initView() {
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        collect()
        setAdapter()
        initNavigatePostingFabClickListener()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    else -> Unit
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is HomeSideEffect.ShowSnackBar -> Snackbar.make(binding.root, SnackbarType.GHOST).show()
                    is HomeSideEffect.DismissBottomSheet -> findNavController().popBackStack()
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

        override fun onLikeBtnClick(id: Long) {
            toast("like")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            handleProfileNavigation(id)
        }

        override fun onFeedImageClick(image: String) {
            feedActionHandler.onImageClick(image)
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            feedActionHandler.onKebabBtnClick(
                feedId,
                postAuthorId,
                fetchUserType = { viewModel.fetchUserType(it) },
                removeFeed = { viewModel.removeFeed(it) },
                binding.root,
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
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewModel.updateFeeds().collectLatest { pagingData ->
                val transformedPagingData = pagingData.map { FeedTransformer.handleFeedsData(it, binding.root.context) }
                feedAdapter.submitData(transformedPagingData)
            }
        }
        setSwipeLayout()
    }

    private fun setSwipeLayout() {
        binding.layoutHomeSwipe.setOnRefreshListener {
            feedAdapter.refresh()
            binding.layoutHomeSwipe.isRefreshing = false
        }
    }

    private fun initNavigatePostingFabClickListener() {
        binding.fabHomeNavigatePosting.setOnClickListener {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Posting)
        }
    }
}
