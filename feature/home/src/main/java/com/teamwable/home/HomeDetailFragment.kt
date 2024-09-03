package com.teamwable.home

import android.content.res.ColorStateList
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import com.teamwable.home.databinding.FragmentHomeDetailBinding
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.FeedImageDialog
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.hideKeyboard
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.ui.util.Arg.FEED_ID
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.CommentActionHandler
import com.teamwable.ui.util.FeedActionHandler
import com.teamwable.ui.util.FeedTransformer
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.net.URLEncoder

@AndroidEntryPoint
class HomeDetailFragment : BindingFragment<FragmentHomeDetailBinding>(FragmentHomeDetailBinding::inflate) {
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
    private val args: HomeDetailFragmentArgs by navArgs()
    private val viewModel: HomeDetailViewModel by viewModels()
    private lateinit var commentActionHandler: CommentActionHandler
    private lateinit var feedActionHandler: FeedActionHandler

    private var isCommentNull = true
    private var totalCommentLength = 0

    override fun initView() {
        commentActionHandler = CommentActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        val commentSnackbar = Snackbar.make(binding.root, SnackbarType.COMMENT_ING)
        viewModel.updateHomeDetail(args.feedId)
        collect(commentSnackbar)
        initBackBtnClickListener()
    }

    private fun collect(commentSnackbar: Snackbar) {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is HomeDetailUiState.Success -> setLayout(uiState.feed, commentSnackbar)

                    is HomeDetailUiState.RemoveComment -> {
                        findNavController().popBackStack()
                        commentAdapter.removeComment(uiState.commentId)
                    }

                    is HomeDetailUiState.RemoveFeed -> {
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }

                    else -> Unit
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is HomeDetailSideEffect.ShowCommentSnackBar -> {
                        commentSnackbar.updateToCommentComplete()
                        commentAdapter.refresh()
                    }

                    is HomeDetailSideEffect.ShowGhostSnackBar -> Snackbar.make(binding.root, SnackbarType.GHOST).show()
                }
            }
        }
    }

    private fun setLayout(feed: Feed, commentSnackbar: Snackbar) {
        submitFeedList(feed)
        submitCommentList(feed)
        concatAdapter()
        initEditTextHint(feed.postAuthorNickname)
        initEditTextBtn(feed.feedId, commentSnackbar)
    }

    private fun initEditTextHint(nickname: String) {
        binding.etHomeDetailCommentInput.hint = getString(R.string.hint_home_detail_comment_input, nickname)
    }

    private fun initEditTextBtn(contentId: Long, commentSnackbar: Snackbar) {
        binding.run {
            etHomeDetailCommentInput.doAfterTextChanged {
                isCommentNull = etHomeDetailCommentInput.text.isNullOrBlank()
                totalCommentLength = etHomeDetailCommentInput.text.length
                handleUploadBtn(isCommentNull, totalCommentLength, contentId, commentSnackbar)
            }
        }
    }

    private fun handleUploadBtn(isCommentNull: Boolean, totalCommentLength: Int, contentId: Long, commentSnackbar: Snackbar) {
        when {
            (!isCommentNull && totalCommentLength <= POSTING_MAX) -> {
                setUploadingBtnSrc(
                    null,
                    com.teamwable.common.R.drawable.ic_home_comment_upload_btn_active,
                ) {
                    binding.ibHomeDetailCommentInputUpload.isEnabled = true
                    initUploadingActivateBtnClickListener(contentId, commentSnackbar)
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

    private fun initUploadingActivateBtnClickListener(contentId: Long, commentSnackbar: Snackbar) {
        binding.ibHomeDetailCommentInputUpload.setOnClickListener {
            viewModel.addComment(contentId, binding.etHomeDetailCommentInput.text.toString())
            commentSnackbar.show()
            binding.etHomeDetailCommentInput.text.clear()
            requireActivity().hideKeyboard(binding.root)
        }
    }

    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {}

        override fun onGhostBtnClick(postAuthorId: Long, feedId: Long) {
            feedActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateFeedGhost(Ghost(stringOf(AlarmTriggerType.CONTENT.type), postAuthorId, feedId))
            }
        }

        override fun onLikeBtnClick(id: Long) {
            toast("like")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            handleProfileNavigation(id)
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
                binding.root,
            )
        }

        override fun onCommentBtnClick(feedId: Long) {
            binding.etHomeDetailCommentInput.requestFocus()
        }
    }

    private fun onClickCommentItem() = object : CommentClickListener {
        override fun onGhostBtnClick(postAuthorId: Long, commentId: Long) {
            commentActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateCommentGhost(Ghost(stringOf(AlarmTriggerType.COMMENT.type), postAuthorId, commentId))
            }
        }

        override fun onLikeBtnClick(id: Long) {
            toast("commentlike")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            handleProfileNavigation(id)
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            commentActionHandler.onKebabBtnClick(
                feedId,
                postAuthorId,
                fetchUserType = { viewModel.fetchUserType(it) },
                removeComment = { viewModel.removeComment(it) },
                binding.root,
            )
        }

        override fun onItemClick(feedId: Long) {}
    }

    private fun handleProfileNavigation(id: Long) {
        when (viewModel.fetchUserType(id)) {
            ProfileUserType.AUTH -> (activity as Navigation).navigateToProfileAuthFragment()
            ProfileUserType.MEMBER -> findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to id))
            ProfileUserType.EMPTY -> return
        }
    }

    private fun submitFeedList(feed: Feed) {
        viewLifeCycleScope.launch {
            flowOf(PagingData.from(listOf(feed))).collectLatest { pagingData ->
                val transformedPagingData = pagingData.map { FeedTransformer.handleFeedsData(it, binding.root.context) }
                feedAdapter.submitData(transformedPagingData)
            }
        }
    }

    private fun submitCommentList(feed: Feed) {
        viewLifeCycleScope.launch {
            viewModel.updateComments(feed.feedId).collectLatest { pagingData ->
                val transformedPagingData = pagingData.map { FeedTransformer.handleCommentsData(it, binding.root.context) }
                commentAdapter.submitData(transformedPagingData)
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
        setSwipeLayout()
    }

    private fun setSwipeLayout() {
        val feedId = arguments?.getLong(FEED_ID)
        binding.layoutHomeSwipe.setOnRefreshListener {
            if (feedId != null) viewModel.updateHomeDetail(feedId)
            commentAdapter.refresh()
            binding.layoutHomeSwipe.isRefreshing = false
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
