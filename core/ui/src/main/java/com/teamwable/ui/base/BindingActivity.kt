package com.teamwable.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.teamwable.ui.extensions.hideKeyboard

abstract class BindingActivity<T : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> T
) : AppCompatActivity() {
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    protected abstract fun initView()

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: View(this))
        return super.dispatchTouchEvent(ev)
    }
}