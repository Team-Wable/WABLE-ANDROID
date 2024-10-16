package com.teamwable.homedetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_WRITE_COMMENT
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.home.R
import com.teamwable.home.databinding.FragmentHomeDetailBinding
import com.teamwable.model.Comment
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.hideKeyboard
import com.teamwable.ui.extensions.setOnDuplicateBlockClick
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.shareAdapter.CommentViewHolder
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.shareAdapter.FeedViewHolder
import com.teamwable.ui.shareAdapter.PagingLoadingAdapter
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.ui.util.Arg.FEED_ID
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.BundleKey.FEED_STATE
import com.teamwable.ui.util.BundleKey.HOME_DETAIL_RESULT
import com.teamwable.ui.util.BundleKey.IS_FEED_REMOVED
import com.teamwable.ui.util.CommentActionHandler
import com.teamwable.ui.util.FeedActionHandler
import com.teamwable.ui.util.FeedTransformer
import com.teamwable.ui.util.Navigation
import com.teamwable.ui.util.SingleEventHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeDetailFragment : BindingFragment<FragmentHomeDetailBinding>(FragmentHomeDetailBinding::inflate) {
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
    private val args: HomeDetailFragmentArgs by navArgs()
    private val viewModel: HomeDetailViewModel by viewModels()
    private lateinit var commentActionHandler: CommentActionHandler
    private lateinit var feedActionHandler: FeedActionHandler
    private val singleEventHandler: SingleEventHandler by lazy { SingleEventHandler.from() }

    private var isCommentNull = true
    private var totalCommentLength = 0
    private var isCommentAdded = false

    override fun initView() {
        commentActionHandler = CommentActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        val commentSnackbar = Snackbar.make(binding.root, SnackbarType.COMMENT_ING)
        viewModel.updateHomeDetailToNetwork(args.feedId)
        collect(commentSnackbar)
        initBackBtnClickListener()
    }

    private fun collect(commentSnackbar: Snackbar) {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is HomeDetailUiState.Success -> setLayout(uiState.feed, commentSnackbar)

                    is HomeDetailUiState.RemoveFeed -> {
                        saveFeedStateResult(Feed(feedId = uiState.feedId), isRemoved = true)
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }

                    is HomeDetailUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    is HomeDetailUiState.Loading -> Unit
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is HomeDetailSideEffect.ShowCommentSnackBar -> {
                        commentSnackbar.updateToCommentComplete()
                        isCommentAdded = true
                        commentAdapter.refresh()
                    }

                    is HomeDetailSideEffect.ShowSnackBar -> Snackbar.make(binding.root, sideEffect.type).show()
                    is HomeDetailSideEffect.DismissBottomSheet -> findNavController().popBackStack()
                }
            }
        }
    }

    private fun setLayout(feed: Feed, commentSnackbar: Snackbar) {
        submitFeedList(feed)
        submitCommentList(feed)
        concatAdapter()
        scrollToBottomOnCommentAdded()
        initEditTextHint(feed.postAuthorNickname)
        initEditTextBtn(feed.feedId, commentSnackbar)
        initRvClickListenerToHideKeyboard()
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
        binding.ibHomeDetailCommentInputUpload.setOnDuplicateBlockClick {
            trackEvent(CLICK_WRITE_COMMENT)
            viewModel.addComment(contentId, binding.etHomeDetailCommentInput.text.toString())
            commentSnackbar.show()
            binding.etHomeDetailCommentInput.text.clear()
            requireActivity().hideKeyboard(binding.root)
        }
    }

    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            requireActivity().hideKeyboard(binding.root)
        }

        override fun onGhostBtnClick(postAuthorId: Long, feedId: Long) {
            feedActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateGhost(Ghost(stringOf(AlarmTriggerType.CONTENT.type), postAuthorId, feedId))
            }
        }

        override fun onLikeBtnClick(viewHolder: FeedViewHolder, id: Long, isLiked: Boolean) {
            feedActionHandler.onLikeBtnClick(viewHolder, id) { feedId, likeState ->
                singleEventHandler.debounce(coroutineScope = lifecycleScope) {
                    if (isLiked != viewHolder.likeBtn.isChecked) viewModel.updateFeedLike(feedId, likeState)
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

        override fun onCommentBtnClick(feedId: Long) {
            binding.etHomeDetailCommentInput.requestFocus()
        }
    }

    private fun onClickCommentItem() = object : CommentClickListener {
        override fun onGhostBtnClick(postAuthorId: Long, commentId: Long) {
            commentActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateGhost(Ghost(stringOf(AlarmTriggerType.COMMENT.type), postAuthorId, commentId))
            }
        }

        override fun onLikeBtnClick(viewHolder: CommentViewHolder, comment: Comment) {
            commentActionHandler.onLikeBtnClick(viewHolder, comment.commentId) { commentId, likeState ->
                singleEventHandler.debounce(coroutineScope = lifecycleScope) {
                    if (comment.isLiked != viewHolder.likeBtn.isChecked) viewModel.updateCommentLike(commentId, comment.content, likeState)
                }
            }
        }

        override fun onPostAuthorProfileClick(id: Long) {
            handleProfileNavigation(id)
        }

        override fun onKebabBtnClick(comment: Comment) {
            commentActionHandler.onKebabBtnClick(
                comment,
                fetchUserType = { viewModel.fetchUserType(it) },
                removeComment = { viewModel.removeComment(it) },
                reportUser = { nickname, content -> viewModel.reportUser(nickname, content) },
            )
        }

        override fun onItemClick(feedId: Long) {
            requireActivity().hideKeyboard(binding.root)
        }
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
            viewModel.updateHomeDetailToFlow(feed).collectLatest { pagingData ->
                val transformedPagingData = pagingData.map {
                    saveFeedStateResult(it)
                    val transformedFeed = FeedTransformer.handleFeedsData(it, binding.root.context)
                    val isAuth = viewModel.fetchUserType(transformedFeed.postAuthorId) == ProfileUserType.AUTH
                    transformedFeed.copy(isAuth = isAuth)
                }
                feedAdapter.submitData(transformedPagingData)
            }
        }
    }

    private fun submitCommentList(feed: Feed) {
        viewLifeCycleScope.launch {
            viewModel.updateComments(feed.feedId).collectLatest { pagingData ->
                val transformedPagingData = pagingData.map {
                    val transformedFeed = FeedTransformer.handleCommentsData(it, binding.root.context)
                    val isAuth = viewModel.fetchUserType(transformedFeed.postAuthorId) == ProfileUserType.AUTH
                    transformedFeed.copy(isAuth = isAuth, feedId = feed.feedId)
                }
                commentAdapter.submitData(transformedPagingData)
            }
        }
    }

    private fun scrollToBottomOnCommentAdded() {
        var isFirstLoad = true

        viewLifeCycleScope.launch {
            // 답글 아래로 스크롤
            commentAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.source.append is LoadState.NotLoading && isCommentAdded) {
                    binding.rvHomeDetail.smoothScrollToPosition(commentAdapter.itemCount)
                    if (loadStates.append.endOfPaginationReached) isCommentAdded = false
                }

                if (loadStates.source.refresh is LoadState.NotLoading && !isFirstLoad) isFirstLoad = false
            }
        }
    }

    private fun concatAdapter() {
        binding.rvHomeDetail.apply {
            adapter = ConcatAdapter(
                feedAdapter,
                commentAdapter.withLoadStateFooter(PagingLoadingAdapter()),
            )
            if (itemDecorationCount == 0) addItemDecoration(HomeDetailRecyclerViewDivider(binding.root.context))
        }
        setSwipeLayout()
    }

    private fun setSwipeLayout() {
        val feedId = arguments?.getLong(FEED_ID)
        binding.layoutHomeSwipe.setOnRefreshListener {
            if (feedId != null) viewModel.updateHomeDetailToNetwork(feedId)
            binding.layoutHomeSwipe.isRefreshing = false
        }
    }

    private fun initBackBtnClickListener() {
        binding.toolbarHomeDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveFeedStateResult(feed: Feed, isRemoved: Boolean = false) {
        parentFragmentManager.setFragmentResult(
            HOME_DETAIL_RESULT,
            Bundle().apply {
                putParcelable(FEED_STATE, feed)
                putBoolean(IS_FEED_REMOVED, isRemoved)
            },
        )
    }

    private fun initRvClickListenerToHideKeyboard() {
        binding.rvHomeDetail.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child == null) {
                    requireActivity().hideKeyboard(binding.root)
                    return true
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    companion object {
        const val POSTING_MAX = 499
    }
}
