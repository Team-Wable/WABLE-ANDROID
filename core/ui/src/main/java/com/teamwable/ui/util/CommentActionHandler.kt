package com.teamwable.ui.util

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_GHOST_COMMENT
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_LIKE_COMMENT
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.model.home.Comment
import com.teamwable.model.home.LikeState
import com.teamwable.ui.component.BottomSheet
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.component.TwoLabelBottomSheet
import com.teamwable.ui.shareAdapter.LikeableViewHolder
import com.teamwable.ui.type.BanTriggerType
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
    fun onKebabBtnClick(
        comment: Comment,
        fetchUserType: (Long) -> ProfileUserType,
        removeComment: (Long) -> Unit,
        reportUser: (String, String) -> Unit,
        banUser: (Comment, String) -> Unit,
    ) {
        when (fetchUserType(comment.postAuthorId)) {
            ProfileUserType.AUTH -> navigateToBottomSheet(BottomSheetType.DELETE_FEED)
            ProfileUserType.MEMBER -> navigateToBottomSheet(BottomSheetType.REPORT)
            ProfileUserType.ADMIN -> navigateToTwoLabelBottomSheet(BottomSheetType.REPORT, BottomSheetType.BAN)
            ProfileUserType.EMPTY -> return
        }
        handleDialogResult { dialogType ->
            when (dialogType) {
                DialogType.DELETE_COMMENT -> removeComment(comment.commentId)
                DialogType.REPORT -> {
                    navController.popBackStack()
                    reportUser(comment.postAuthorNickname, comment.content)
                }

                DialogType.BAN -> {
                    navController.popBackStack()
                    banUser(comment, BanTriggerType.COMMENT.name.lowercase())
                }

                else -> Unit
            }
        }
    }

    fun onGhostBtnClick(type: DialogType, updateGhost: () -> Unit) {
        trackEvent(CLICK_GHOST_COMMENT)
        navigateToDialog(type)
        handleDialogResult { dialogType ->
            when (dialogType) {
                DialogType.TRANSPARENCY -> updateGhost()
                else -> Unit
            }
        }
    }

    fun onLikeBtnClick(viewHolder: LikeableViewHolder, id: Long, saveLike: (Long, LikeState) -> Unit) {
        val likeCount = viewHolder.likeCountTv.text.toString().toInt()
        val updatedLikeCount = if (viewHolder.likeBtn.isChecked) {
            trackEvent(CLICK_LIKE_COMMENT)
            likeCount + 1
        } else {
            if (likeCount > 0) likeCount - 1 else 0
        }

        viewHolder.likeCountTv.text = updatedLikeCount.toString()
        saveLike(id, LikeState(viewHolder.likeBtn.isChecked, updatedLikeCount.toString()))
    }

    private fun navigateToBottomSheet(type: BottomSheetType) {
        BottomSheet.show(context, navController, type)
        handleBottomSheetResult()
    }

    private fun navigateToTwoLabelBottomSheet(firstType: BottomSheetType, secondType: BottomSheetType) {
        TwoLabelBottomSheet.show(context, navController, firstType, secondType)
        handleBottomSheetResult()
    }

    private fun handleBottomSheetResult() {
        fragmentManager.setFragmentResultListener(BOTTOM_SHEET_RESULT, lifecycleOwner) { _, bundle ->
            when (bundle.getString(BOTTOM_SHEET_TYPE)) {
                BottomSheetType.DELETE_FEED.name -> navigateToDialog(DialogType.DELETE_COMMENT)
                BottomSheetType.REPORT.name -> navigateToDialog(DialogType.REPORT)
                BottomSheetType.BAN.name -> navigateToDialog(DialogType.BAN)
            }
        }
    }

    private fun navigateToDialog(type: DialogType) {
        TwoButtonDialog.show(context, navController, type)
    }

    private fun handleDialogResult(onResult: (DialogType) -> Unit) {
        fragmentManager.setFragmentResultListener(DIALOG_RESULT, lifecycleOwner) { _, bundle ->
            val dialogType = DialogType.valueOf(bundle.getString(DIALOG_TYPE) ?: return@setFragmentResultListener)
            onResult(dialogType)
        }
    }
}
