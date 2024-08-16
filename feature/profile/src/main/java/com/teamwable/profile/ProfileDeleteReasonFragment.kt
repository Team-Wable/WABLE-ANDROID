package com.teamwable.profile

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.FragmentProfileDeleteReasonBinding
import com.teamwable.ui.base.BindingFragment

class ProfileDeleteReasonFragment : BindingFragment<FragmentProfileDeleteReasonBinding>(FragmentProfileDeleteReasonBinding::inflate) {
    private val checkBoxList by lazy {
        with(binding) {
            listOf(
                cbProfileDeleteReasonCheck1,
                cbProfileDeleteReasonCheck2,
                cbProfileDeleteReasonCheck3,
                cbProfileDeleteReasonCheck4,
                cbProfileDeleteReasonCheck5,
                cbProfileDeleteReasonCheck6,
                cbProfileDeleteReasonCheck7
            )
        }
    }

    override fun initView() {
        setAppbarText()
        initBackBtnClickListener()
        initCheckBoxClickListener()
        updateButtonState()
    }

    override fun onResume() {
        super.onResume()
        updateButtonState()
    }

    private fun updateButtonState() {
        val anyChecked = checkBoxList.any { it.isChecked }
        binding.btnProfileDeleteReasonNext.apply {
            isEnabled = anyChecked
            if (isEnabled) {
                setTextColor(context.getColor(com.teamwable.ui.R.color.white))
                setOnClickListener {
                    navigateUpToProfileDeleteConfirmFragment()
                }
            } else setTextColor(context.getColor(com.teamwable.ui.R.color.gray_600))
        }
    }

    private fun initCheckBoxClickListener() {
        checkBoxList.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                updateButtonState()
            }
        }
    }

    private fun setAppbarText() {
        binding.viewProfileDeleteReasonAppbar.tvProfileAppbarTitle.text = getString(R.string.appbar_profile_delete_title)
    }

    private fun initBackBtnClickListener() {
        binding.viewProfileDeleteReasonAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateUpToProfileDeleteConfirmFragment() {
        findNavController().navigate(R.id.action_navigation_profile_delete_reason_to_navigation_profile_delete_confirm)
    }
}
