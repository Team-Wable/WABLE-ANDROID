package com.teamwable.home

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.teamwable.home.databinding.FragmentHomeDetailBinding
import com.teamwable.model.Comment
import com.teamwable.model.Feed
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeDetailFragment : BindingFragment<FragmentHomeDetailBinding>(FragmentHomeDetailBinding::inflate) {
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
    private val args: HomeDetailFragmentArgs by navArgs()

    override fun initView() {
        setFeedAdapter()
        setCommentAdapter()
        concatAdapter()
        initBackBtnClickListener()
    }

    // TODO : test용 toast 지우기
    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {}

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

        override fun onCommentBtnClick(feedId: Long) {
            // TODO : comment 달기 action
        }
    }

    private fun onClickCommentItem() = object : CommentClickListener {
        override fun onGhostBtnClick(postAuthorId: Long) {
            toast("commentghost")
        }

        override fun onLikeBtnClick(id: Long) {
            toast("commentlike")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            toast("commentprofile")
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            toast("commentkebab")
        }
    }

    private fun setFeedAdapter() {
        submitFeedList()
    }

    private fun submitFeedList() {
        feedAdapter.submitList(listOf(args.content))
    }

    private fun setCommentAdapter() {
        submitCommentList()
    }

    // TODO : mock data 지우기
    private fun submitCommentList() {
        val mock = mutableListOf<Comment>()
        repeat(5) {
            mock.add(
                Comment(
                    postAuthorId = 0,
                    postAuthorProfile = "",
                    postAuthorNickname = "페이커최고",
                    commentId = 0,
                    content = "어떤 순간에도 너를 찾을 수 있게 반대가 끌리는 천만번째 이유를 내일의 우리는 알지도 몰라 오늘따라 왠지",
                    uploadTime = "5",
                    isPostAuthorGhost = false,
                    postAuthorGhost = 100,
                    isLiked = false,
                    likedNumber = "100",
                    postAuthorTeamTag = "T1",
                ),
            )
        }
        commentAdapter.submitList(mock)
    }

    private fun concatAdapter() {
        binding.rvHomeDetail.apply {
            adapter = ConcatAdapter(
                feedAdapter,
                commentAdapter,
            )
            if (itemDecorationCount == 0) {
                setDivider(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
            }
        }
    }

    private fun initBackBtnClickListener() {
        binding.toolbarHomeDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
