package com.teamwable.ui.component

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.databinding.BottomsheetBinding
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_RESULT
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_TYPE

class BottomSheet() : BindingBottomSheetFragment<BottomsheetBinding>(BottomsheetBinding::inflate) {
    private lateinit var type: BottomSheetType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomSheetTypeString = requireArguments().getString(BOTTOM_SHEET_TYPE)
        if (bottomSheetTypeString != null)
            type = BottomSheetType.valueOf(bottomSheetTypeString)
    }

    override fun initView() {
        initLayout()
        initTitleClickListener()
    }

    private fun initLayout() {
        binding.tvBottomsheetTitle.text = stringOf(type.title)
    }

    private fun initTitleClickListener() {
        binding.tvBottomsheetTitle.setOnClickListener {
            val result = Bundle().apply { putString(BOTTOM_SHEET_TYPE, type.name) }
            setFragmentResult(BOTTOM_SHEET_RESULT, result)
        }
    }

    companion object {
        fun show(
            context: Context,
            navController: NavController,
            bottomSheetType: BottomSheetType,
        ) = BottomSheet().apply {
            navigateToBottomSheet(context, navController, bottomSheetType)
        }

        private fun navigateToBottomSheet(context: Context, navController: NavController, bottomSheetType: BottomSheetType) =
            navController.deepLinkNavigateTo(context, DeepLinkDestination.BottomSheet, mapOf(BOTTOM_SHEET_TYPE to bottomSheetType.name))
    }
}
