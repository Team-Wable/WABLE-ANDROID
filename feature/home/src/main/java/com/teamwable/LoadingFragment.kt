package com.teamwable

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.teamwable.home.R
import com.teamwable.home.databinding.FragmentLoadingBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.viewLifeCycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class LoadingFragment : BindingFragment<FragmentLoadingBinding>(FragmentLoadingBinding::inflate) {
    override fun initView() {
        blockNavigateToBack()
        initLoadingContent()
        initAnimation()
    }

    private fun blockNavigateToBack() = activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {}

    private fun initAnimation() {
        viewLifeCycleScope.launch {
            delay(1600)
            val fadeOutAnim =
                AnimationUtils.loadAnimation(context, R.anim.anim_loading_fade_out)
            fadeOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    findNavController().popBackStack()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.layoutLoading.startAnimation(fadeOutAnim)
        }
    }

    private fun initLoadingContent() {
        val loadingContents = resources.getStringArray(R.array.loading_contents)
        val randomIndex = Random.nextInt(loadingContents.size)
        val randomContent = loadingContents[randomIndex]

        binding.tvLoading.text = randomContent
    }
}
