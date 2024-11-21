package com.teamwable.profile.profile

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.teamwable.model.Profile
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.load
import com.teamwable.ui.type.TeamTag
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
abstract class BindingProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    protected val binding: FragmentProfileBinding
        get() = requireNotNull(_binding) { "ViewBinding is not initialized" }

    private lateinit var offsetChangedListener: AppBarLayout.OnOffsetChangedListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::offsetChangedListener.isInitialized)
            binding.appbarProfileInfo.removeOnOffsetChangedListener(offsetChangedListener)
        _binding = null
    }

    protected fun setLayout(data: Profile) = with(binding) {
        viewProfileAppbar.tvProfileAppbarTitle.text = data.nickName
        ivProfileImg.load(data.profileImg)
        tvProfileNickname.text = data.nickName
        tvProfileInfo.text = getString(R.string.label_profile_info, data.teamTag.ifBlank { TeamTag.LCK.name }, data.lckYears)
        tvProfileGhostPercentage.text = getString(R.string.label_ghost_percentage, data.ghost)
        tvProfileLevel.text = getString(R.string.label_profile_level, data.level)
        setSwipeLayout()
        setGhostProgress(data.ghost)
    }

    private fun setSwipeLayout() {
        offsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.layoutProfileSwipe.isEnabled = verticalOffset == 0
        }
        binding.appbarProfileInfo.addOnOffsetChangedListener(offsetChangedListener)
    }

    private fun setGhostProgress(percentage: Int) {
        animateProgress(abs(100 + percentage))
        if (percentage < -50) setGhostProgressColor(com.teamwable.ui.R.color.sky_50) else setGhostProgressColor(com.teamwable.ui.R.color.purple_50)
    }

    private fun setGhostProgressColor(@ColorRes color: Int) = with(binding) {
        progressProfileGhost.setIndicatorColor(colorOf(color))
        ivProfileGhostIcon.imageTintList = ContextCompat.getColorStateList(requireContext(), color)
    }

    private fun animateProgress(targetProgress: Int) {
        val animator = ObjectAnimator.ofInt(binding.progressProfileGhost, "progress", 0, targetProgress)
        animator.duration = 1000
        animator.start()
    }
}
