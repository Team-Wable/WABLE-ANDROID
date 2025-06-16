package com.teamwable.viewit.viewit

import android.os.Bundle
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.BottomSheet
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.openUri
import com.teamwable.ui.extensions.setupEnumResultListener
import com.teamwable.ui.type.BanTriggerType
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.ui.type.toDialogType
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_RESULT
import com.teamwable.ui.util.Arg.BOTTOM_SHEET_TYPE
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import com.teamwable.ui.util.Arg.DIALOG_TYPE
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.BundleKey.POSTING_RESULT
import com.teamwable.ui.util.BundleKey.VIEW_IT_CONTENT
import com.teamwable.ui.util.BundleKey.VIEW_IT_LINK
import com.teamwable.ui.util.Navigation
import com.teamwable.viewit.databinding.FragmentViewItComposeBinding
import com.teamwable.viewit.ui.ViewItIntent
import com.teamwable.viewit.ui.ViewItRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewItFragment : BindingFragment<FragmentViewItComposeBinding>(FragmentViewItComposeBinding::inflate) {
    private val viewModel: ViewItViewModel by viewModels()

    override fun initView() {
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    ViewItRoute(
                        onShowBottomSheet = ::showBottomSheet,
                        onShowSnackBar = ::showSnackBar,
                        onNavigateToMemberProfile = ::navigateToMemberProfile,
                        onNavigateToMyProfile = ::navigateToMyProfile,
                        onNavigateToError = ::navigateToError,
                        onDismissBottomSheet = ::dismissBottomSheet,
                        onOpenUrl = { openUri(it) },
                        onNavigateToPosting = ::navigateToPosting,
                    )
                }
            }
        }
    }

    private fun showSnackBar(type: SnackbarType, throwable: Throwable? = null) = Snackbar.make(binding.root, type, throwable).show()

    private fun navigateToMyProfile() = (activity as Navigation).navigateToProfileAuthFragment()

    private fun navigateToMemberProfile(id: Long) = findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to id))

    private fun dismissBottomSheet() = findNavController().popBackStack()

    private fun navigateToError() = (activity as Navigation).navigateToErrorFragment()

    private fun showBottomSheet(type: BottomSheetType, info: ViewIt) {
        BottomSheet.show(binding.root.context, findNavController(), type)
        handleBottomSheetResult(info)
    }

    private fun handleBottomSheetResult(info: ViewIt) {
        setupEnumResultListener<BottomSheetType>(BOTTOM_SHEET_RESULT, BOTTOM_SHEET_TYPE) {
            showDialog(it.toDialogType(), info)
        }
    }

    private fun showDialog(type: DialogType, info: ViewIt) {
        TwoButtonDialog.show(binding.root.context, findNavController(), type)
        handleDialogResult(info)
    }

    private fun handleDialogResult(info: ViewIt) {
        setupEnumResultListener<DialogType>(DIALOG_RESULT, DIALOG_TYPE) {
            when (it) {
                DialogType.DELETE_FEED -> viewModel.onIntent(ViewItIntent.RemoveViewIt(info.viewItId))
                DialogType.REPORT -> viewModel.onIntent(ViewItIntent.ReportViewIt(info.postAuthorNickname, info.viewItContent))
                DialogType.BAN -> viewModel.onIntent(ViewItIntent.BanViewIt(Triple(info.postAuthorId, BanTriggerType.CONTENT.name.lowercase(), info.viewItId)))
                else -> Unit
            }
        }
    }

    private fun navigateToPosting() {
        findNavController().navigate(ViewItFragmentDirections.actionViewItToPosting())
        handlePostingResult()
    }

    private fun handlePostingResult() {
        parentFragmentManager.setFragmentResultListener(POSTING_RESULT, viewLifecycleOwner) { _, result ->
            val (link, content) = result.extractViewItData()

            if (link.isNotBlank() && content.isNotBlank()) {
                viewModel.onIntent(ViewItIntent.PostViewIt(link, content))
            }
        }
    }

    private fun Bundle.extractViewItData(): Pair<String, String> {
        val link = getString(VIEW_IT_LINK).orEmpty()
        val content = getString(VIEW_IT_CONTENT).orEmpty()
        return link to content
    }
}
