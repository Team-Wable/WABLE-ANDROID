package com.teamwable.profile.hamburger

import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileDeleteReasonBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf

class ProfileDeleteReasonFragment : BindingFragment<FragmentProfileDeleteReasonBinding>(FragmentProfileDeleteReasonBinding::inflate) {
    private lateinit var checkBoxList: List<CheckBox>

    override fun initView() {
        setAppbarText()
        initBackBtnClickListener()
        initCheckBoxList()
        initCheckBoxClickListener()
        updateButtonState()
    }

    private fun initCheckBoxList() = with(binding) {
        checkBoxList = listOf(
            cbProfileDeleteReasonCheck1,
            cbProfileDeleteReasonCheck2,
            cbProfileDeleteReasonCheck3,
            cbProfileDeleteReasonCheck4,
            cbProfileDeleteReasonCheck5,
            cbProfileDeleteReasonCheck6,
            cbProfileDeleteReasonCheck7,
        )
    }

    private fun updateButtonState() {
        val anyChecked = checkBoxList.any { it.isChecked }
        binding.btnProfileDeleteReasonNext.apply {
            isEnabled = anyChecked
            if (isEnabled) {
                setTextColor(colorOf(com.teamwable.ui.R.color.white))
                setOnClickListener {
                    navigateUpToProfileDeleteConfirmFragment()
                }
            } else {
                setTextColor(colorOf(com.teamwable.ui.R.color.gray_600))
            }
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

    private fun getSelectedReasons(): Array<String> {
        return checkBoxList.filter { it.isChecked }
            .map { it.text.toString() }
            .toTypedArray()
    }

    private fun navigateUpToProfileDeleteConfirmFragment() {
        ProfileDeleteReasonFragmentDirections.actionNavigationProfileDeleteReasonToNavigationProfileDeleteConfirm(getSelectedReasons())
            .also { findNavController().navigate(it) }
    }
}
