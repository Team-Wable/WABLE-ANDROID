package com.teamwable.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.teamwable.common.uistate.UiState
import com.teamwable.main.databinding.ActivityMainBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initView() {
        setBottomNavigation()

        setupNumberObserve()
    }

    private fun setupNumberObserve() {
        viewModel.notificationNumberUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    if (it.data > 0) {
                        setBadgeOnNotification(true)
                    } else if (it.data < 0) {
                        Timber.tag("main").e("알맞지 않은 notification number get : ${it.data}")
                    }
                }

                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bnvMain.apply {
            itemIconTintList = null
            setupWithNavController(navController)
        }

        initBottomNavigationChangedListener(navController)
    }

    private fun initBottomNavigationChangedListener(navController: NavController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            handleBottomNavigationVisibility(destination)
            if (destination.id == com.teamwable.notification.R.id.navigation_notification) setBadgeOnNotification(false)
        }
    }

    // TODO : list안에 bottomnavi 없는 것들을 추가해주세요
    private fun handleBottomNavigationVisibility(destination: NavDestination) {
        binding.groupMainBnv.visible(
            destination.id !in
                listOf(
                    com.teamwable.posting.R.id.navigation_posting,
                    com.teamwable.profile.R.id.navigation_profile_delete_confirm,
                    com.teamwable.profile.R.id.navigation_profile_delete_reason,
                    com.teamwable.profile.R.id.navigation_profile_information,
                    com.teamwable.home.R.id.navigation_home_detail,
                    com.teamwable.profile.R.id.navigation_profile_more,
                ),
        )
    }

    private fun setBadgeOnNotification(isVisible: Boolean) {
        binding.bnvMain.getOrCreateBadge(R.id.graph_notification).apply {
            this.isVisible = isVisible
            horizontalOffset = 1
            if (isVisible) backgroundColor = colorOf(com.teamwable.ui.R.color.error) else clearNumber()
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
