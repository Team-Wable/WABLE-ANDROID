package com.teamwable.home

import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import com.teamwable.home.databinding.FragmentHomeDetailBinding
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
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
    private val args: HomeDetailFragmentArgs by navArgs()
    private val viewModel: HomeDetailViewModel by viewModels()

    private var isCommentNull = true
    private var totalCommentLength = 0

    private val dummyNickname = "배차은우"

    override fun initView() {
        submitFeedList()
        submitCommentList()
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

    private fun submitFeedList() {
        viewLifeCycleScope.launch {
            flowOf(PagingData.from(listOf(args.content))).collectLatest { pagingData ->
                feedAdapter.submitData(pagingData)
            }
        }
    }

    private fun submitCommentList() {
        val feed = args.content as? Feed ?: return
        viewLifeCycleScope.launch {
            viewModel.updateComments(feed.feedId).collectLatest { pagingData ->
                commentAdapter.submitData(pagingData)
            }
        }
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
