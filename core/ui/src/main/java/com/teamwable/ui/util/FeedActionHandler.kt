package com.teamwable.ui.util

import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_DELETE_POST
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_GHOST_POST
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_LIKE_POST
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.model.home.Feed
import com.teamwable.model.home.LikeState
import com.teamwable.ui.component.BottomSheet
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.component.TwoLabelBottomSheet
import com.teamwable.ui.component.feedimage.FeedImageDialog
import com.teamwable.ui.type.BanTriggerType
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_RESULT
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_TYPE
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import com.teamwable.ui.util.Arg.DIALOG_TYPE
import java.net.URLEncoder

class FeedActionHandler(
    private val context: Context,
    private val navController: NavController,
    private val fragmentManager: FragmentManager,
    private val lifecycleOwner: LifecycleOwner,
) {
    fun onKebabBtnClick(
        feed: Feed,
        fetchUserType: (Long) -> ProfileUserType,
        removeFeed: (Long) -> Unit,
        reportUser: (String, String) -> Unit,
        banUser: (Feed, String) -> Unit,
    ) {
        when (fetchUserType(feed.postAuthorId)) {
            ProfileUserType.AUTH -> navigateToBottomSheet(BottomSheetType.DELETE_FEED)
            ProfileUserType.MEMBER -> navigateToBottomSheet(BottomSheetType.REPORT)
            ProfileUserType.ADMIN -> navigateToTwoLabelBottomSheet(BottomSheetType.REPORT, BottomSheetType.BAN)
            ProfileUserType.EMPTY -> return
        }
        handleDialogResult { dialogType ->
            when (dialogType) {
                DialogType.DELETE_FEED -> {
                    trackEvent(CLICK_DELETE_POST)
                    removeFeed(feed.feedId)
                }

                DialogType.REPORT -> {
                    navController.popBackStack()
                    reportUser(feed.postAuthorNickname, "${feed.title}\n${feed.content}")
                }

                DialogType.BAN -> {
                    navController.popBackStack()
                    banUser(feed, BanTriggerType.CONTENT.name.lowercase())
                }

                else -> Unit
            }
        }
    }

    fun onGhostBtnClick(type: DialogType, updateGhost: () -> Unit) {
        trackEvent(CLICK_GHOST_POST)
        navigateToDialog(type)
        handleDialogResult { dialogType ->
            when (dialogType) {
                DialogType.TRANSPARENCY -> updateGhost()
                else -> Unit
            }
        }
    }

    fun onImageClick(image: String) {
        val encodedUrl = URLEncoder.encode(image, "UTF-8")
        FeedImageDialog.show(context, navController, encodedUrl)
    }

    fun onLikeBtnClick(likeInfo: LikeInfo) {
        val likeCount = likeInfo.likeCountTv.text.toString().toInt()
        val updatedLikeCount = if (likeInfo.likeBtn.isChecked) {
            trackEvent(CLICK_LIKE_POST)
            likeCount + 1
        } else {
            if (likeCount > 0) likeCount - 1 else 0
        }

        likeInfo.likeCountTv.text = updatedLikeCount.toString()
        likeInfo.saveLike(likeInfo.id, LikeState(likeInfo.likeBtn.isChecked, updatedLikeCount.toString()))
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
                BottomSheetType.DELETE_FEED.name -> navigateToDialog(DialogType.DELETE_FEED)
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

data class LikeInfo(
    val likeBtn: CheckBox,
    val likeCountTv: TextView,
    val id: Long,
    val saveLike: (Long, LikeState) -> Unit,
)
