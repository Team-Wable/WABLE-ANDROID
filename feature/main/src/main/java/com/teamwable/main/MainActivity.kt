package com.teamwable.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamwable.main.databinding.ActivityMainBinding
import com.teamwable.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: DummyViewModel by viewModels()

    override fun initView() {
        viewModel.loadDummy()
        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { uiState ->
                    when (uiState) {
                        DummyUiState.Loading -> Timber.d("loading")
                        is DummyUiState.LoadSuccess -> binding.dummy.text =
                            uiState.dummy[0].contentText

                        is DummyUiState.Error -> Timber.e(uiState.exception)
                    }
                }
        }
    }
}