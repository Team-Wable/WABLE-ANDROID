package com.teamwable.home

import androidx.navigation.fragment.findNavController
import com.teamwable.home.databinding.FragmentHomeBinding
import com.teamwable.model.Feed
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }

    override fun initView() {
        setAdapter()
        initNavigatePostingFabClickListener()
    }

    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            toast("item")
            findNavController().navigate(HomeFragmentDirections.actionHomeToHomeDetail(feed))
        }

        override fun onGhostBtnClick(postAuthorId: Long) {
            toast("ghost")
        }

        override fun onLikeBtnClick(id: Long) {
            toast("like")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            toast("profile")
        }

        override fun onFeedImageClick(image: String) {
            toast("image")
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            toast("kebab")
        }

        override fun onCommentBtnClick(feedId: Long) {}
    }

    private fun setAdapter() {
        binding.rvHome.apply {
            adapter = feedAdapter
            if (itemDecorationCount == 0) {
                setDivider(com.teamwable.ui.R.drawable.recyclerview_item_divider)
            }
        }
        submitList()
    }

    private fun submitList() {
        val mock = mutableListOf<Feed>()
        repeat(5) {
            mock.add(
                Feed(
                    postAuthorId = 0,
                    postAuthorProfile = "",
                    postAuthorNickname = "페이커최고",
                    feedId = 0,
                    title = "내가 S면 넌 나의 N이 되어줘 ",
                    content = "어떤 순간에도 너를 찾을 수 있게 반대가 끌리는 천만번째 이유를 내일의 우리는 알지도 몰라 오늘따라 왠지 말이 꼬여 성을 빼고 부르는 건 아직 어색해 (지훈아..!) 너와 나 우리 모여보니까 같은 색 더듬이가 자라나 창문 너머의 춤을 따라 \n" +
                        "https://www.wable.com",
                    uploadTime = "5",
                    isPostAuthorGhost = false,
                    postAuthorGhost = 100,
                    isLiked = false,
                    likedNumber = "100",
                    commentNumber = "200",
                    image = "",
                    postAuthorTeamTag = "T1",
                ),
            )
        }
        repeat(5) {
            mock.add(
                Feed(
                    postAuthorId = 0,
                    postAuthorProfile = "",
                    postAuthorNickname = "hihi",
                    feedId = 0,
                    title = "내가 S면 넌 나의 N이 되어줘 ",
                    content = "어떤 순간에도 너를 찾을 수 있게 반대가 끌리는 천만번째 이유를 내일의 우리는 알지도 몰라 오늘따라 왠지 말이 꼬여 성을 빼고 부르는 건 아직 어색해 (지훈아..!) 너와 나 우리 모여보니까 같은 색 더듬이가 자라나 창문 너머의 춤을 따라 \n" +
                        "https://www.wable.com",
                    uploadTime = "5",
                    isPostAuthorGhost = false,
                    postAuthorGhost = 100,
                    isLiked = true,
                    likedNumber = "100",
                    commentNumber = "200",
                    image = "",
                    postAuthorTeamTag = "GEN",
                ),
            )
        }
        feedAdapter.submitList(mock)
    }

    private fun initNavigatePostingFabClickListener() {
        binding.fabHomeNavigatePosting.setOnClickListener {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Posting)
        }
    }
}
