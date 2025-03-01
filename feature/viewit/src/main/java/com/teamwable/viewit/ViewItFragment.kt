package com.teamwable.viewit

import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.setDividerWithPadding
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.shareAdapter.PagingLoadingAdapter
import com.teamwable.viewit.databinding.FragmentViewItBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class ViewItFragment : BindingFragment<FragmentViewItBinding>(FragmentViewItBinding::inflate) {
    private lateinit var viewItAdapter: ViewItAdapter

    override fun initView() {
        setAdapter()
        if (this::viewItAdapter.isInitialized) submitList()
        setOnPostingBtnClickListener()
    }

    override fun onDestroyView() {
        binding.rvViewIt.adapter = null
        super.onDestroyView()
    }

    private fun setAdapter() {
        viewItAdapter = ViewItAdapter()
        binding.rvViewIt.apply {
            adapter = viewItAdapter.withLoadStateFooter(PagingLoadingAdapter())
            if (itemDecorationCount == 0) setDividerWithPadding(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
        }
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            dummyPagingData.collectLatest { pagingData ->
                viewItAdapter.submitData(pagingData)
            }
        }
    }

    private fun setOnPostingBtnClickListener() {
        binding.fabViewItPosting.setOnClickListener {
            findNavController().navigate(ViewItFragmentDirections.actionViewItToPosting())
        }
    }

    private val dummyList = List(20) {
        ViewIt(
            postAuthorId = 12345L,
            postAuthorProfile = "",
            postAuthorNickname = "페이커",
            viewItId = it.toLong(),
            linkImage = "https://github.com/user-attachments/assets/fa1fac23-5126-4037-9050-cb80481d98c3",
            linkName = "www.naver.com",
            linkTitle = "링크 제목 링크 제목 링크 제목 링크 제목 링크 제목 링크 제목 링크 제목",
            viewItContent = "뷰잇 멘트는 최대 50자만 가능해요. 뷰잇 멘트는뷰잇멘트는 최대 50자만 가능해요. 뷰잇 멘트는 50자만",
            uploadTime = "2025-03-01T12:00:00Z",
            isLiked = false,
            likedNumber = "100",
            isBlind = false,
        )
    }

    private val dummyPagingData: Flow<PagingData<ViewIt>> = flowOf(PagingData.from(dummyList))
}
