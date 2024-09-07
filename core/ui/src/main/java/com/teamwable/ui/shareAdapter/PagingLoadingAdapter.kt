package com.teamwable.ui.shareAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.ui.R
import com.teamwable.ui.databinding.ItemPagingLoadingBinding
import com.teamwable.ui.extensions.visible

class PagingLoadingAdapter : LoadStateAdapter<PagingLoadingAdapter.PagingLoadingViewHolder>() {
    class PagingLoadingViewHolder(
        private val binding: ItemPagingLoadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.ivPagingLoading.visible(loadState is LoadState.Loading)
            val rotateAnimation = AnimationUtils.loadAnimation(itemView.context, R.anim.spinner_rotate)
            binding.ivPagingLoading.startAnimation(rotateAnimation)
        }
    }

    override fun onBindViewHolder(holder: PagingLoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): PagingLoadingViewHolder {
        val binding =
            ItemPagingLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingLoadingViewHolder(binding)
    }
}
