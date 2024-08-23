package com.teamwable.home

import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import com.teamwable.home.databinding.FragmentHomeDetailBinding
import com.teamwable.model.Comment
import com.teamwable.model.Feed
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.type.SnackbarType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeDetailFragment : BindingFragment<FragmentHomeDetailBinding>(FragmentHomeDetailBinding::inflate) {
    val mock = mutableListOf<Comment>()

    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
    private val args: HomeDetailFragmentArgs by navArgs()

    private var isCommentNull = true
    private var totalCommentLength = 0

    private val dummyNickname = "배차은우"

    override fun initView() {
        setFeedAdapter()
        setCommentAdapter()
        concatAdapter()
        initBackBtnClickListener()

        initEditTextHint()
        initEditTextBtn()
    }

    private fun initEditTextHint() {
        binding.etHomeDetailCommentInput.hint = getString(R.string.hint_home_detail_comment_input, dummyNickname)
    }

    private fun initEditTextBtn() {
        binding.run {
            etHomeDetailCommentInput.doAfterTextChanged {
                isCommentNull = etHomeDetailCommentInput.text.isNullOrBlank()
                totalCommentLength = etHomeDetailCommentInput.text.length
                handleUploadBtn(isCommentNull, totalCommentLength)
            }
        }
    }

    private fun handleUploadBtn(isCommentNull: Boolean, totalCommentLength: Int) {
        when {
            (!isCommentNull && totalCommentLength <= POSTING_MAX) -> {
                setUploadingBtnSrc(
                    null,
                    com.teamwable.common.R.drawable.ic_home_comment_upload_btn_active,
                ) {
                    binding.ibHomeDetailCommentInputUpload.isEnabled = true
                    initUploadingActivateBtnClickListener()
                }
            }

            else -> {
                setUploadingBtnSrc(
                    ColorStateList.valueOf(colorOf(com.teamwable.ui.R.color.gray_100)),
                    com.teamwable.common.R.drawable.ic_home_comment_upload_btn_inactive,
                ) {
                    binding.ibHomeDetailCommentInputUpload.isEnabled = false
                }
            }
        }
    }

    private fun setUploadingBtnSrc(
        backgroundTintResId: ColorStateList?,
        btnResId: Int,
        clickListener: () -> Unit,
    ) = with(binding) {
        etHomeDetailCommentInput.backgroundTintList = backgroundTintResId
        ibHomeDetailCommentInputUpload.setImageResource(btnResId)
        clickListener.invoke()
    }

    private fun initUploadingActivateBtnClickListener() {
        binding.ibHomeDetailCommentInputUpload.setOnClickListener {
            findNavController().popBackStack()

            Snackbar(requireView(), SnackbarType.COMMENT_ING).apply {
                show()

                mock.add(
                    Comment(
                        postAuthorId = 1,
                        postAuthorProfile = "",
                        postAuthorNickname = "배차은우",
                        commentId = 0,
                        content = binding.etHomeDetailCommentInput.text.toString(),
                        uploadTime = "3",
                        isPostAuthorGhost = false,
                        postAuthorGhost = 100,
                        isLiked = false,
                        likedNumber = "-10000",
                        postAuthorTeamTag = "FOX",
                    ),
                )
                commentAdapter.submitList(mock.toList())

                Handler(Looper.getMainLooper()).postDelayed({
                    updateToCommentComplete()
                }, 2000)
            }
        }
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
        viewLifeCycleScope.launch {
            flowOf(PagingData.from(listOf(args.content))).collectLatest { pagingData ->
                feedAdapter.submitData(pagingData)
            }
        }
    }

    private fun setCommentAdapter() {
        submitCommentList()
    }

    // TODO : mock data 지우기
    private fun submitCommentList() {
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

    companion object {
        const val POSTING_MAX = 499
    }
}
