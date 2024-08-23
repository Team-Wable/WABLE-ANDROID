package com.teamwable.ui.util

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.teamwable.ui.component.BottomSheet
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_RESULT
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_TYPE
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import com.teamwable.ui.util.Arg.DIALOG_TYPE

class CommentActionHandler(
    private val context: Context,
    private val navController: NavController,
    private val fragmentManager: FragmentManager,
    private val lifecycleOwner: LifecycleOwner,
) {
    fun onKebabBtnClick(commentId: Long, postAuthorId: Long, fetchUserType: (Long) -> ProfileUserType, removeComment: (Long) -> Unit) {
        when (fetchUserType(postAuthorId)) {
            ProfileUserType.AUTH -> navigateToBottomSheet(BottomSheetType.DELETE_FEED)
            ProfileUserType.MEMBER -> navigateToBottomSheet(BottomSheetType.REPORT)
            ProfileUserType.EMPTY -> return
        }
        handleDialogResult(commentId, removeComment)
    }

    private fun navigateToBottomSheet(type: BottomSheetType) {
        BottomSheet.show(context, navController, type)
        handleBottomSheetResult()
    }

    private fun handleBottomSheetResult() {
        fragmentManager.setFragmentResultListener(BOTTOM_SHEET_RESULT, lifecycleOwner) { _, bundle ->
            when (bundle.getString(BOTTOM_SHEET_TYPE)) {
                BottomSheetType.DELETE_FEED.name -> navigateToDialog(DialogType.DELETE_FEED)
                BottomSheetType.REPORT.name -> navigateToDialog(DialogType.REPORT)
            }
        }
    }

    private fun navigateToDialog(type: DialogType) {
        TwoButtonDialog.show(context, navController, type)
    }

    private fun handleDialogResult(feedId: Long, removeFeed: (Long) -> Unit) {
        fragmentManager.setFragmentResultListener(DIALOG_RESULT, lifecycleOwner) { _, bundle ->
            when (bundle.getString(DIALOG_TYPE)) {
                DialogType.DELETE_FEED.name -> removeFeed(feedId)
                DialogType.REPORT.name -> Unit
            }
        }
    }
}
