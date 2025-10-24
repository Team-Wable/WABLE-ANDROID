package com.teamwable.news.curation

import androidx.compose.ui.platform.ViewCompositionStrategy
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.news.databinding.FragmentNewsCurationBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.openUri
import com.teamwable.ui.type.SnackbarType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsCurationFragment : BindingFragment<FragmentNewsCurationBinding>(FragmentNewsCurationBinding::inflate) {
    override fun initView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    NewsCurationRoute(
                        onOpenUrl = { openUri(it) },
                        onShowSnackBar = ::showSnackBar,
                    )
                }
            }
        }
    }

    private fun showSnackBar(throwable: Throwable? = null) = Snackbar.make(binding.root, SnackbarType.ERROR, throwable).show()
}
