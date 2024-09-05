package com.teamwable.main

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.teamwable.main.databinding.FragmentErrorBinding
import com.teamwable.ui.base.BindingFragment

class ErrorFragment : BindingFragment<FragmentErrorBinding>(FragmentErrorBinding::inflate) {
    override fun initView() {
        initNavigateToHomeBtnClickListener()
    }

    private fun initNavigateToHomeBtnClickListener() {
        binding.btnErrorNavigateToHome.setOnClickListener {
            val navController = findNavController()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.navigation_error, inclusive = true)
                .build()
            navController.navigate(R.id.graph_home, null, navOptions)
        }
    }

    companion object {
        fun newInstance(navController: NavController) = ErrorFragment().apply {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.graph_home, inclusive = true)
                .build()

            navController.navigate(R.id.navigation_error, null, navOptions)
        }
    }
}
