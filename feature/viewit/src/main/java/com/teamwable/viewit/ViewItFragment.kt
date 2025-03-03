package com.teamwable.viewit

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.setDividerWithPadding
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.PagingLoadingAdapter
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.FeedActionHandler
import com.teamwable.ui.util.Navigation
import com.teamwable.viewit.adapter.ViewItAdapter
import com.teamwable.viewit.adapter.ViewItClickListener
import com.teamwable.viewit.adapter.ViewItViewHolder
import com.teamwable.viewit.databinding.FragmentViewItBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewItFragment : BindingFragment<FragmentViewItBinding>(FragmentViewItBinding::inflate) {
    private val viewModel: ViewItViewModel by viewModels()
    private lateinit var viewItAdapter: ViewItAdapter
    private lateinit var feedActionHandler: FeedActionHandler

    override fun initView() {
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), parentFragmentManager, viewLifecycleOwner)
        collect()
        setAdapter()
        setOnPostingBtnClickListener()
    }

    override fun onDestroyView() {
        binding.rvViewIt.adapter = null
        super.onDestroyView()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is ViewItUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    else -> Unit
                }
            }
        }
    }

    private fun setAdapter() {
        viewItAdapter = ViewItAdapter(onClickViewItItem())
        binding.rvViewIt.apply {
            adapter = viewItAdapter.withLoadStateFooter(PagingLoadingAdapter())
            if (itemDecorationCount == 0) setDividerWithPadding(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
        }
        if (this::viewItAdapter.isInitialized) submitList()
        setSwipeLayout()
        setEmptyView()
    }

    private fun onClickViewItItem() = object : ViewItClickListener {
        override fun onItemClick(link: String) {
            toast("link")
        }

        override fun onLikeBtnClick(viewHolder: ViewItViewHolder, id: Long, isLiked: Boolean) {
            toast("좋아요")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            handleProfileNavigation(id)
        }

        override fun onKebabBtnClick(viewIt: ViewIt) {
            toast("kebab")
        }
    }

    private fun handleProfileNavigation(id: Long) {
        when (viewModel.fetchUserType(id)) {
            ProfileUserType.AUTH -> (activity as Navigation).navigateToProfileAuthFragment()
            in setOf(ProfileUserType.MEMBER, ProfileUserType.ADMIN) -> findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to id))
            else -> return
        }
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateViewIts().collectLatest {
                    viewItAdapter.submitData(it)
                }
            }
        }
    }

    private fun setOnPostingBtnClickListener() {
        binding.fabViewItPosting.setOnClickListener {
            findNavController().navigate(ViewItFragmentDirections.actionViewItToPosting())
        }
    }

    private fun setSwipeLayout() {
        binding.layoutViewItSwipe.setOnRefreshListener {
            binding.layoutViewItSwipe.isRefreshing = false
            viewItAdapter.refresh()
        }
    }

    private fun setEmptyView() {
        viewLifeCycleScope.launch {
            viewLifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewItAdapter.loadStateFlow.collectLatest { loadStates ->
                    val isEmptyList = loadStates.refresh is LoadState.NotLoading && viewItAdapter.itemCount == 0
                    binding.tvEmpty.visible(isEmptyList)
                }
            }
        }
    }
}
