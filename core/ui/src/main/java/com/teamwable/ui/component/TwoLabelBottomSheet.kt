package com.teamwable.ui.component

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.databinding.BottomsheetTwoLabelBinding
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_FIRST_TYPE
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_RESULT
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_SECOND_TYPE
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_TYPE

class TwoLabelBottomSheet() : BindingBottomSheetFragment<BottomsheetTwoLabelBinding>(BottomsheetTwoLabelBinding::inflate) {
    private lateinit var firstLabelType: BottomSheetType
    private lateinit var secondLabelType: BottomSheetType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstLabelType = BottomSheetType.valueOf(arguments?.getString(BOTTOM_SHEET_FIRST_TYPE) ?: return)
        secondLabelType = BottomSheetType.valueOf(arguments?.getString(BOTTOM_SHEET_SECOND_TYPE) ?: return)
    }

    override fun initView() {
        if (this::firstLabelType.isInitialized && this::secondLabelType.isInitialized) {
            initLayout()
            initLabelClickListener()
        }
    }

    private fun initLayout() {
        binding.tvBottomsheetFirstLabel.text = stringOf(firstLabelType.title)
        binding.tvBottomsheetSecondLabel.text = stringOf(secondLabelType.title)
    }

    private fun initLabelClickListener() {
        binding.tvBottomsheetFirstLabel.setOnClickListener {
            setBottomSheetResult(firstLabelType.name)
        }

        binding.tvBottomsheetSecondLabel.setOnClickListener {
            setBottomSheetResult(secondLabelType.name)
        }
    }

    private fun setBottomSheetResult(type: String) {
        val result = Bundle().apply { putString(BOTTOM_SHEET_TYPE, type) }
        setFragmentResult(BOTTOM_SHEET_RESULT, result)
    }

    companion object {
        fun show(
            context: Context,
            navController: NavController,
            bottomSheetFirstType: BottomSheetType,
            bottomSheetSecondType: BottomSheetType,
        ) = TwoLabelBottomSheet().apply {
            navigateToBottomSheet(context, navController, bottomSheetFirstType, bottomSheetSecondType)
        }

        private fun navigateToBottomSheet(context: Context, navController: NavController, bottomSheetFirstType: BottomSheetType, bottomSheetSecondType: BottomSheetType) =
            navController.deepLinkNavigateTo(
                context, DeepLinkDestination.TwoLabelBottomSheet,
                mapOf(BOTTOM_SHEET_FIRST_TYPE to bottomSheetFirstType.name, BOTTOM_SHEET_SECOND_TYPE to bottomSheetSecondType.name),
            )
    }
}
