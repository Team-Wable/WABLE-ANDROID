package com.teamwable.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamwable.home.databinding.FragmentHomeBinding
import com.teamwable.model.Feed
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.FeedImageDialog
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.setDividerWithPadding
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.FeedActionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.URLEncoder

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
                    is HomeUiState.RemoveFeed -> {
                        findNavController().popBackStack()
                        feedAdapter.removeFeed(uiState.feedId)
                    }

                    else -> Unit
                }
            }
        }
    }

    // TODO : test용 toast 지우기
    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            toast("item")
            findNavController().navigate(HomeFragmentDirections.actionHomeToHomeDetail(feed.feedId))
        }

        override fun onGhostBtnClick(postAuthorId: Long) {
            toast("ghost")
        }

        override fun onLikeBtnClick(id: Long) {
            toast("like")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to id))
        }

        override fun onFeedImageClick(image: String) {
            val encodedUrl = URLEncoder.encode(image, "UTF-8")
            FeedImageDialog.Companion.show(requireContext(), findNavController(), encodedUrl)
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            feedActionHandler.onKebabBtnClick(
                feedId,
                postAuthorId,
                fetchUserType = { viewModel.fetchUserType(it) },
                removeFeed = { viewModel.removeFeed(it) },
            )
        }

        override fun onCommentBtnClick(feedId: Long) {}
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
                feedAdapter.submitData(pagingData)
            }
        }
    }

    private fun initNavigatePostingFabClickListener() {
        binding.fabHomeNavigatePosting.setOnClickListener {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Posting)
        }
    }
}
