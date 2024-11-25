package com.teamwable.news.detail

import androidx.navigation.fragment.navArgs
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.NewsTabType
import com.teamwable.news.databinding.FragmentNewsDetailBinding
import com.teamwable.ui.base.BindingFragment

class NewsDetailFragment : BindingFragment<FragmentNewsDetailBinding>(FragmentNewsDetailBinding::inflate) {
    private val args: NewsDetailFragmentArgs by navArgs()
    private val notice: NewsInfoModel by lazy { args.newsInfoModel }
    private val newsTabType: NewsTabType by lazy { args.newsTabType }

    override fun initView() {
        receiveResultFromNotice()
    }

    private fun receiveResultFromNotice() {
        binding.tvNewsDetail.text = notice.newsText
    }
}
