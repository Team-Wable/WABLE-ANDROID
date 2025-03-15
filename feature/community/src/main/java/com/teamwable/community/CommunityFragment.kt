package com.teamwable.community

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.teamwable.common.util.LinkStorage
import com.teamwable.community.databinding.FragmentCommunityBinding
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.openUri
import com.teamwable.ui.type.SnackbarType

class CommunityFragment : BindingFragment<FragmentCommunityBinding>(FragmentCommunityBinding::inflate) {
    override fun initView() {
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    CommunityRoute(
                        navigateToGoogleForm = ::navigateToGoogleForm,
                        navigateToPushAlarm = ::navigateToPushAlarm,
                        onShowErrorSnackBar = ::showSnackBar,
                    )
                }
            }
        }
    }

    private fun navigateToGoogleForm() {
        openUri(LinkStorage.GOOGLE_FORM_LINK)
    }

    private fun navigateToPushAlarm() {
        findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.PushAlarm)
    }

    private fun showSnackBar(throwable: Throwable) {
        Snackbar.make(binding.root, SnackbarType.ERROR, throwable).show()
    }
}
