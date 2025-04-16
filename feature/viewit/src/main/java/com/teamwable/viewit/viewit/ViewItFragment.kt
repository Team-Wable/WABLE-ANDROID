package com.teamwable.viewit.viewit

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.ui.base.BindingFragment
import com.teamwable.viewit.databinding.FragmentViewItComposeBinding
import com.teamwable.viewit.ui.ViewItRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewItFragment : BindingFragment<FragmentViewItComposeBinding>(FragmentViewItComposeBinding::inflate) {
    private val viewModel: ViewItViewModel by viewModels()
//    private lateinit var viewItAdapter: ViewItAdapter
//    private lateinit var feedActionHandler: FeedActionHandler
//    private val singleEventHandler: SingleEventHandler by lazy { SingleEventHandler.from() }

    override fun initView() {
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    ViewItRoute()
                }
            }
        }
    }
//    override fun initView() {
//        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
//        collect()
//        setAdapter()
//        setOnPostingBtnClickListener()
//        fetchViewItUploaded()
//        scrollToTopOnRefresh()
//    }

//    override fun onDestroyView() {
//        binding.rvViewIt.adapter = null
//        super.onDestroyView()
//    }
//
//    private fun collect() {
//        viewLifeCycleScope.launch {
//            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
//                when (uiState) {
//                    is ViewItUiState.Error -> (activity as Navigation).navigateToErrorFragment()
//                    is ViewItUiState.Success -> viewItAdapter.refresh()
//                    else -> Unit
//                }
//            }
//        }
//
//        viewLifeCycleScope.launch {
//            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
//                when (sideEffect) {
//                    is ViewItSideEffect.ShowSnackBar -> showSnackBar(sideEffect.type)
//                    is ViewItSideEffect.DismissBottomSheet -> findNavController().popBackStack()
//                    is ViewItSideEffect.ShowErrorMessage -> showSnackBar(SnackbarType.ERROR, sideEffect.throwable)
//                }
//            }
//        }
//    }
//
//    private fun setAdapter() {
//        viewItAdapter = ViewItAdapter(onClickViewItItem())
//        binding.rvViewIt.apply {
//            adapter = viewItAdapter.withLoadStateFooter(PagingLoadingAdapter())
//            if (itemDecorationCount == 0) setDividerWithPadding(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
//        }
//        if (this::viewItAdapter.isInitialized) submitList()
//        setSwipeLayout()
//        setEmptyView()
//    }
//
//    private fun onClickViewItItem() = object : ViewItClickListener {
//        override fun onItemClick(link: String) {
//            openUri(link)
//        }
//
//        override fun onLikeBtnClick(viewHolder: ViewItViewHolder, id: Long, isLiked: Boolean) {
//            feedActionHandler.onLikeBtnClick(
//                LikeInfo(viewHolder.likeBtn, viewHolder.likeCountTv, id) { feedId, likeState ->
//                    if (singleEventHandler.canProceed(FEED_LIKE)) {
//                        if (isLiked != viewHolder.likeBtn.isChecked) viewModel.updateLike(feedId, likeState)
//                    }
//                },
//            )
//        }
//
//        override fun onPostAuthorProfileClick(id: Long) {
//            handleProfileNavigation(id)
//        }
//
//        override fun onKebabBtnClick(viewIt: ViewIt) {
//            feedActionHandler.onKebabBtnClick(
//                viewIt.toFeed(),
//                fetchUserType = { viewModel.fetchUserType(it) },
//                removeFeed = { viewModel.removeViewIt(viewIt.viewItId) },
//                reportUser = { nickname, content -> viewModel.reportUser(nickname, content) },
//                banUser = { trigger, _ -> viewModel.banUser(Triple(trigger.postAuthorId, stringOf(BanTriggerType.VIEWIT.type), trigger.feedId)) },
//            )
//        }
//    }
//
//    private fun handleProfileNavigation(id: Long) {
//        when (viewModel.fetchUserType(id)) {
//            ProfileUserType.AUTH -> (activity as Navigation).navigateToProfileAuthFragment()
//            in setOf(ProfileUserType.MEMBER, ProfileUserType.ADMIN) -> findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to id))
//            else -> return
//        }
//    }
//
//    private fun submitList() {
//        viewLifeCycleScope.launch {
//            viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.updateViewIts().collectLatest {
//                    viewItAdapter.submitData(it)
//                }
//            }
//        }
//    }
//
//    private fun setOnPostingBtnClickListener() {
//        binding.fabViewItPosting.setOnClickListener {
//            findNavController().navigate(ViewItFragmentDirections.actionViewItToPosting())
//        }
//    }
//
//    private fun setSwipeLayout() {
//        binding.layoutViewItSwipe.setOnRefreshListener {
//            binding.layoutViewItSwipe.isRefreshing = false
//            viewItAdapter.refresh()
//        }
//    }
//
//    private fun setEmptyView() {
//        viewLifeCycleScope.launch {
//            viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewItAdapter.loadStateFlow.collectLatest { loadStates ->
//                    val isEmptyList = loadStates.refresh is LoadState.NotLoading && viewItAdapter.itemCount == 0
//                    binding.tvEmpty.visible(isEmptyList)
//                }
//            }
//        }
//    }
//
//    private fun fetchViewItUploaded() {
//        parentFragmentManager.setFragmentResultListener(POSTING_RESULT, viewLifecycleOwner) { _, result ->
//            val (link, content) = result.extractViewItData()
//
//            if (link.isNotBlank() && content.isNotBlank()) {
//                showSnackBar(SnackbarType.VIEW_IT_ING)
//                viewModel.postViewIt(link, content)
//            }
//        }
//    }
//
//    private fun Bundle.extractViewItData(): Pair<String, String> {
//        val link = getString(VIEW_IT_LINK).orEmpty()
//        val content = getString(VIEW_IT_CONTENT).orEmpty()
//        return link to content
//    }
//
//    private fun scrollToTopOnRefresh() {
//        var isFirstLoad = true
//
//        viewLifeCycleScope.launch {
//            viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewItAdapter.loadStateFlow.collectLatest { loadStates ->
//                    if (loadStates.source.refresh is LoadState.Loading) isFirstLoad = false
//
//                    if (loadStates.source.refresh is LoadState.NotLoading && !isFirstLoad) {
//                        binding.rvViewIt.scrollToPosition(0)
//                        isFirstLoad = true
//                    }
//                }
//            }
//        }
//    }
//
//    private fun showSnackBar(snackbarType: SnackbarType, throwable: Throwable? = null) {
//        Snackbar.make(binding.root, snackbarType, throwable).show()
//    }
}
