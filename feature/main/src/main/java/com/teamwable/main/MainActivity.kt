package com.teamwable.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.teamwable.common.uistate.UiState
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_HOME_BOTNAVI
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_MYPROFILE_BOTNAVI
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_NEWS_BOTNAVI
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_NOTI_BOTNAVI
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.home.HomeFragment
import com.teamwable.main.databinding.ActivityMainBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigation {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var appUpdateHelper: AppUpdateHandler

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                toast(getString(R.string.label_in_app_update_fail))
            } else {
                toast(getString(R.string.label_in_app_update_success))
            }
        }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInAppUpdate()
        initView()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onResume() {
        super.onResume()
        appUpdateHelper.resumeUpdateIfNeeded(activityResultLauncher)
    }

    private fun setInAppUpdate() {
        appUpdateHelper = AppUpdateHandler(this)
        appUpdateHelper.checkForAppUpdate(activityResultLauncher)
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
        initBottomNaviSelectedListener(navController)
        initBottomNaviReSelectedListener(navController)
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
                    com.teamwable.profile.R.id.navigation_profile_member,
                    com.teamwable.ui.R.id.navigation_feed_image_dialog,
                    com.teamwable.ui.R.id.navigation_bottomSheet,
                    com.teamwable.home.R.id.navigation_loading,
                    R.id.navigation_error,
                    com.teamwable.ui.R.id.navigation_two_button_dialog,
                    com.teamwable.profile.R.id.navigation_profile_edit,
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

    override fun navigateToProfileAuthFragment() {
        binding.bnvMain.selectedItemId = R.id.graph_profile
    }

    override fun navigateToErrorFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController
        ErrorFragment.newInstance(navController)
    }

    override fun navigateToNewsFragment() {
        binding.bnvMain.selectedItemId = R.id.graph_news
    }

    private fun initBottomNaviSelectedListener(navController: NavController) {
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.graph_home -> {
                    trackEvent(CLICK_HOME_BOTNAVI)
                    lifecycleScope.launch {
                        delay(100)
                        findHomeFragment()?.updateToLoadingState()
                    }
                }

                R.id.graph_news -> trackEvent(CLICK_NEWS_BOTNAVI)
                R.id.graph_notification -> trackEvent(CLICK_NOTI_BOTNAVI)
                R.id.graph_profile -> trackEvent(CLICK_MYPROFILE_BOTNAVI)
            }

            it.onNavDestinationSelected(navController)
        }
    }

    private fun initBottomNaviReSelectedListener(navController: NavController) {
        binding.bnvMain.setOnItemReselectedListener {
            if (it.itemId == R.id.graph_home) {
                lifecycleScope.launch {
                    delay(100)
                    findHomeFragment()?.refreshHome()
                }
            }
            it.onNavDestinationSelected(navController)
        }
    }

    private fun findHomeFragment(): HomeFragment? = supportFragmentManager.findFragmentById(R.id.fcv_main)?.let { hostFragment ->
        hostFragment.childFragmentManager.fragments.firstOrNull { it is HomeFragment } as? HomeFragment
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
