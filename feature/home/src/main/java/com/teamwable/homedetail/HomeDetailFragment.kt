package com.teamwable.homedetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_UPLOAD_COMMENT
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_WRITE_COMMENT
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_WRITE_RECOMMENT
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.home.R
import com.teamwable.home.databinding.FragmentHomeDetailBinding
import com.teamwable.model.home.Comment
import com.teamwable.model.home.Feed
import com.teamwable.model.home.Ghost
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.hideKeyboard
import com.teamwable.ui.extensions.setOnDuplicateBlockClick
import com.teamwable.ui.extensions.showKeyboard
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentAdapter.Companion.PARENT_COMMENT_DEFAULT
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.shareAdapter.FeedViewHolder
import com.teamwable.ui.shareAdapter.LikeableViewHolder
import com.teamwable.ui.shareAdapter.PagingLoadingAdapter
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.CommentType
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
import com.teamwable.ui.util.LikeInfo
import com.teamwable.ui.util.Navigation
import com.teamwable.ui.util.SingleEventHandler
import com.teamwable.ui.util.ThrottleKey.COMMENT_LIKE
import com.teamwable.ui.util.ThrottleKey.FEED_LIKE
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

    private lateinit var commentAdapterObserver: AdapterDataObserver

    private var isCommentNull = true
    private var totalCommentLength = 0

    override fun initView() {
        commentActionHandler = CommentActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        val commentSnackbar = Snackbar.make(binding.root, SnackbarType.COMMENT_ING)
        val childCommentSnackbar = Snackbar.make(binding.root, SnackbarType.CHILD_COMMENT_ING)
        viewModel.updateHomeDetailToNetwork(args.feedId)
        collect(commentSnackbar, childCommentSnackbar)
        initBackBtnClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::commentAdapterObserver.isInitialized) commentAdapter.unregisterAdapterDataObserver(commentAdapterObserver)
    }

    private fun collect(commentSnackbar: Snackbar, childCommentSnackbar: Snackbar) {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is HomeDetailUiState.Success -> setLayout(uiState.feed, commentSnackbar, childCommentSnackbar)

                    is HomeDetailUiState.RemoveFeed -> {
                        saveFeedStateResult(Feed(feedId = uiState.feedId), isRemoved = true)
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }

                    is HomeDetailUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    is HomeDetailUiState.Loading -> binding.pbHomeDetailLoading.visible(true)
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is HomeDetailSideEffect.ShowCommentSnackBar -> {
                        commentSnackbar.updateToCommentComplete(SnackbarType.COMMENT_COMPLETE)
                        commentAdapter.refresh()
                    }

                    is HomeDetailSideEffect.ShowSnackBar -> Snackbar.make(binding.root, sideEffect.type).show()
                    is HomeDetailSideEffect.ShowChildCommentSnackBar -> {
                        childCommentSnackbar.updateToCommentComplete(SnackbarType.CHILD_COMMENT_COMPLETE)
                        commentAdapter.refresh()
                    }

                    is HomeDetailSideEffect.DismissBottomSheet -> findNavController().popBackStack()
                }
            }
        }
    }

    private fun setLayout(feed: Feed, commentSnackbar: Snackbar, childCommentSnackbar: Snackbar) {
        submitFeedList(feed)
        submitCommentList(feed)
        handleCommentLoadingState()
        concatAdapter()
        scrollToBottomOnCommentAdded()
        initEditTextHint(feed.postAuthorNickname, CommentType.PARENT)
        initEditTextBtn(feed, commentSnackbar, childCommentSnackbar)
        initRvClickListenerToHideKeyboard()
    }

    private fun initEditTextHint(nickname: String, type: CommentType) = when (type) {
        CommentType.PARENT -> binding.etHomeDetailCommentInput.hint = getString(R.string.hint_home_detail_comment_input, nickname)
        CommentType.CHILD -> binding.etHomeDetailCommentInput.hint = getString(R.string.hint_home_detail_child_comment_input, nickname)
    }

    private fun initEditTextBtn(feed: Feed, commentSnackbar: Snackbar, childCommentSnackbar: Snackbar) {
        binding.run {
            etHomeDetailCommentInput.doAfterTextChanged {
                isCommentNull = etHomeDetailCommentInput.text.isNullOrBlank()
                totalCommentLength = etHomeDetailCommentInput.text.length
                handleUploadBtn(isCommentNull, totalCommentLength, feed, commentSnackbar, childCommentSnackbar)
            }
        }
    }

    private fun handleUploadBtn(isCommentNull: Boolean, totalCommentLength: Int, feed: Feed, commentSnackbar: Snackbar, childCommentSnackbar: Snackbar) {
        when {
            (!isCommentNull && totalCommentLength <= POSTING_MAX) -> {
                setUploadingBtnSrc(
                    null,
                    com.teamwable.common.R.drawable.ic_home_comment_upload_btn_active,
                ) {
                    binding.ibHomeDetailCommentInputUpload.isEnabled = true
                    initUploadingActivateBtnClickListener(feed, commentSnackbar, childCommentSnackbar)
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

    private fun initUploadingActivateBtnClickListener(feed: Feed, commentSnackbar: Snackbar, childCommentSnackbar: Snackbar) {
        binding.ibHomeDetailCommentInputUpload.setOnDuplicateBlockClick {
            trackEvent(CLICK_UPLOAD_COMMENT)
            viewModel.addComment(feed.feedId, binding.etHomeDetailCommentInput.text.toString())
            if (viewModel.parentCommentIds.first == PARENT_COMMENT_DEFAULT) commentSnackbar.show() else childCommentSnackbar.show()
            handleCommentBtnClick(feed.postAuthorNickname, CommentType.PARENT)
            binding.root.context.hideKeyboard(it)
        }
    }

    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            requireActivity().hideKeyboard(binding.root)
        }

        override fun onGhostBtnClick(postAuthorId: Long, feedId: Long) {
            feedActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) { reason ->
                viewModel.updateGhost(Ghost(stringOf(AlarmTriggerType.CONTENT.type), postAuthorId, feedId, reason))
            }
        }

        override fun onLikeBtnClick(viewHolder: FeedViewHolder, id: Long, isLiked: Boolean) {
            feedActionHandler.onLikeBtnClick(
                LikeInfo(viewHolder.likeBtn, viewHolder.likeCountTv, id) { feedId, likeState ->
                    if (singleEventHandler.canProceed(FEED_LIKE)) {
                        if (isLiked != viewHolder.likeBtn.isChecked) viewModel.updateFeedLike(feedId, likeState)
                    }
                },
            )
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
                banUser = { trigger, banType -> viewModel.banUser(Triple(trigger.postAuthorId, banType, trigger.feedId)) },
            )
        }

        override fun onCommentBtnClick(postAuthorNickname: String, feedId: Long) {
            trackEvent(CLICK_WRITE_COMMENT)
            handleCommentBtnClick(postAuthorNickname, CommentType.PARENT)
            viewModel.setParentCommentIds(PARENT_COMMENT_DEFAULT, PARENT_COMMENT_DEFAULT)
        }
    }

    private fun handleCommentBtnClick(nickname: String, commentType: CommentType) {
        initEditTextHint(nickname, commentType)
        binding.etHomeDetailCommentInput.text.clear()
        binding.root.context.showKeyboard(binding.etHomeDetailCommentInput)
    }

    private fun onClickCommentItem() = object : CommentClickListener {
        override fun onGhostBtnClick(postAuthorId: Long, commentId: Long) {
            commentActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateGhost(Ghost(stringOf(AlarmTriggerType.COMMENT.type), postAuthorId, commentId))
            }
        }

        override fun onLikeBtnClick(viewHolder: LikeableViewHolder, comment: Comment) {
            commentActionHandler.onLikeBtnClick(viewHolder, comment.commentId) { commentId, likeState ->
                if (singleEventHandler.canProceed(COMMENT_LIKE)) {
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
                banUser = { trigger, banType -> viewModel.banUser(Triple(trigger.postAuthorId, banType, trigger.commentId)) },
            )
        }

        override fun onItemClick(feedId: Long) {
            requireActivity().hideKeyboard(binding.root)
        }

        override fun onChildCommentClick(comment: Comment) {
            trackEvent(CLICK_WRITE_RECOMMENT)
            handleCommentBtnClick(comment.postAuthorNickname, CommentType.CHILD)
            viewModel.setParentCommentIds(comment.commentId, comment.postAuthorId)
        }
    }

    private fun handleProfileNavigation(id: Long) {
        when (viewModel.fetchUserType(id)) {
            ProfileUserType.AUTH -> (activity as Navigation).navigateToProfileAuthFragment()
            in setOf(ProfileUserType.MEMBER, ProfileUserType.ADMIN) -> findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to id))
            else -> return
        }
    }

    private fun submitFeedList(feed: Feed) {
        viewLifeCycleScope.launch {
            viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    private fun submitCommentList(feed: Feed) {
        viewLifeCycleScope.launch {
            viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    private fun scrollToBottomOnCommentAdded() {
        commentAdapterObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvHomeDetail.smoothScrollToPosition(positionStart + 1)
            }
        }.apply { commentAdapter.registerAdapterDataObserver(this) }
    }

    private fun handleCommentLoadingState() = viewLifeCycleScope.launch {
        viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            commentAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.pbHomeDetailLoading.visible(loadStates.refresh is LoadState.Loading)
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
