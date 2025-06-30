package com.teamwable.viewit.benchmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.shareAdapter.PagingLoadingAdapter
import com.teamwable.viewit.adapter.ViewItAdapter
import com.teamwable.viewit.adapter.ViewItClickListener
import com.teamwable.viewit.adapter.ViewItViewHolder
import com.teamwable.viewit.benchmark.BenchmarkComposeEntryActivity.Companion.generateMockViewItData
import com.teamwable.viewit.databinding.FragmentViewItBinding
import kotlinx.coroutines.launch

// TODO :: 컴포즈 마이그레이션이 안정화되면 XML 성능 분석은 제거 예정
class BenchmarkXmlEntryActivity : AppCompatActivity() {
    private lateinit var binding: FragmentViewItBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentViewItBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupMockData()
    }

    private fun setupMockData() {
        val mockItems = generateMockViewItData()
        val viewitAdapter = ViewItAdapter(object : ViewItClickListener {
            override fun onItemClick(link: String) {}

            override fun onLikeBtnClick(viewHolder: ViewItViewHolder, id: Long, isLiked: Boolean) {}

            override fun onPostAuthorProfileClick(id: Long) {}

            override fun onKebabBtnClick(viewIt: ViewIt) {}
        })

        binding.rvViewIt.apply {
            adapter = viewitAdapter.withLoadStateFooter(PagingLoadingAdapter())
        }

        lifecycleScope.launch {
            viewitAdapter.submitData(mockItems)
        }
    }
}
