package com.teamwable.community

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
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
                        onShowErrorSnackBar = ::showSnackBar
                    )
                }
            }
        }
    }

    private fun navigateToGoogleForm() {
        openUri("https://forms.gle/WWfbHXvGNgXMxgZr5")
    }

    private fun navigateToPushAlarm() {
        findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.PushAlarm)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, SnackbarType.ERROR, message).show()
    }
}
