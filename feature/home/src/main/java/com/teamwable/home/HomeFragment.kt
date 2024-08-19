package com.teamwable.home

import androidx.navigation.fragment.findNavController
import com.teamwable.home.databinding.FragmentHomeBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun initView() {
        initNavigatePostingFabClickListener()
    }

    private fun initNavigatePostingFabClickListener() {
        binding.fabHomeNavigatePosting.setOnClickListener {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Posting)
        }
    }
}
