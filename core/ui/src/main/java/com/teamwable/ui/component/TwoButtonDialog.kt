package com.teamwable.ui.component

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.teamwable.common.util.AmplitudeAuthTag.CLICK_COMPLETE_LOGOUT
import com.teamwable.common.util.AmplitudeAuthTag.CLICK_NEXT_DELETEACCOUNT
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_APPLYGHOST_POPUP
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_WITHDRAWGHOST_POPUP
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.ui.base.BindingDialogFragment
import com.teamwable.ui.databinding.DialogTwoButtonBinding
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.dialogFragmentResize
import com.teamwable.ui.extensions.hideKeyboard
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import com.teamwable.ui.util.Arg.DIALOG_TYPE

class TwoButtonDialog() : BindingDialogFragment<DialogTwoButtonBinding>(DialogTwoButtonBinding::inflate) {
    private lateinit var type: DialogType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialogTypeString = requireArguments().getString(DIALOG_TYPE)
        if (dialogTypeString != null)
            type = DialogType.valueOf(dialogTypeString)
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 30f)
    }

    override fun initView() {
        initLayout()
        initNoBtnClickListener()
        initYesBtnClickListener()
    }

    private fun initLayout() = with(binding) {
        tvDialogTwoButtonTitle.text = stringOf(type.title)
        tvDialogTwoButtonDescription.apply {
            if (stringOf(type.description).isBlank()) visible(false) else text = stringOf(type.description)
        }
        tvDialogTwoButtonTitle.setTextAppearance(type.titleTypo)
        btnDialogTwoButtonYes.text = stringOf(type.yesLabel)
        btnDialogTwoButtonNo.text = stringOf(type.noLabel)
        layoutDialogTwoButtonReason.visible(stringOf(type.reason).isNotEmpty())
        etDialogTwoButtonReason.hint = stringOf(type.reason)
        binding.root.apply { setOnClickListener { context.hideKeyboard(it) } }
    }

    private fun initNoBtnClickListener() {
        binding.btnDialogTwoButtonNo.setOnClickListener {
            when (type) {
                DialogType.TRANSPARENCY -> trackEvent(CLICK_WITHDRAWGHOST_POPUP)
                else -> Unit
            }

            findNavController().popBackStack()
        }
    }

    private fun initYesBtnClickListener() {
        binding.btnDialogTwoButtonYes.setOnClickListener {
            when (type) {
                DialogType.TRANSPARENCY -> trackEvent(CLICK_APPLYGHOST_POPUP)
                DialogType.DELETE_ACCOUNT -> trackEvent(CLICK_NEXT_DELETEACCOUNT)
                DialogType.LOGOUT -> trackEvent(CLICK_COMPLETE_LOGOUT)
                else -> Unit
            }

            val result = Bundle().apply {
                putString(DIALOG_TYPE, type.name)
                putString(DIALOG_RESULT, binding.etDialogTwoButtonReason.text.toString())
            }
            setFragmentResult(DIALOG_RESULT, result)
            findNavController().popBackStack()
        }
    }

    companion object {
        fun show(
            context: Context,
            navController: NavController,
            dialogType: DialogType,
        ) = TwoButtonDialog().apply {
            navigateToDialog(context, navController, dialogType)
        }

        private fun navigateToDialog(context: Context, navController: NavController, dialogType: DialogType) =
            navController.deepLinkNavigateTo(context, DeepLinkDestination.TwoButtonDialog, mapOf(DIALOG_TYPE to dialogType.name))
    }
}
