package com.teamwable.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_HOME_BOTNAVI
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_MYPROFILE_BOTNAVI
import com.teamwable.common.util.AmplitudeHomeTag.CLICK_NEWS_BOTNAVI
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.home.HomeFragment
import com.teamwable.main.databinding.ActivityMainBinding
import com.teamwable.ui.extensions.setStatusBarColor
import com.teamwable.ui.extensions.showAlertDialog
import com.teamwable.ui.extensions.statusBarModeOf
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import com.teamwable.viewit.R as viewitR

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigation {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateHelper: AppUpdateHandler
    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                toast(getString(R.string.label_in_app_update_fail))
            } else {
                toast(getString(R.string.label_in_app_update_success))
            }
        }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Timber.i("Download Complete")
            lifecycleScope.launch {
                delay(1000)
                appUpdateManager.completeUpdate()
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInAppUpdate()
        initView()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }

    private fun setInAppUpdate() {
        appUpdateManager.registerListener(installStateUpdatedListener)
        appUpdateHelper = AppUpdateHandler(appUpdateManager).apply {
            checkForAppUpdate { appUpdateInfo -> showUpdateDialog(appUpdateInfo) }
        }
    }

    private fun showUpdateDialog(appUpdateInfo: AppUpdateInfo) {
        val updateType = appUpdateHelper.checkUpdateType(appUpdateInfo)
        val negativeText = if (updateType == UpdateType.MAJOR) "" else getString(R.string.label_in_app_update_next)
        showAlertDialog(
            title = getString(R.string.label_in_app_update_title),
            message = getString(R.string.label_in_app_update_content),
            positiveButtonText = getString(R.string.label_in_app_update_yes),
            negativeButtonText = negativeText,
            onPositiveClick = { appUpdateHelper.startUpdate(appUpdateInfo, activityResultLauncher) },
        )
        // TODO : 업데이트 type이 patch일 때 datastore에 안내했음 여부 값 저장
    }

    private fun initView() {
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController

        navController.setGraph(R.navigation.graph_main)

        binding.bnvMain.apply {
            itemIconTintList = null
            setupWithNavController(navController)
        }

        initBottomNavigationChangedListener(navController)
        initBottomNaviSelectedListener(navController)
        initBottomNaviReSelectedListener(navController)
        updateStatusBarColor(navController)
    }

    private fun initBottomNavigationChangedListener(navController: NavController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            handleBottomNavigationVisibility(destination)
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
                    com.teamwable.ui.R.id.navigation_two_label_bottomsheet,
                    com.teamwable.news.R.id.navigation_news_detail,
                    com.teamwable.notification.R.id.navigation_notification,
                ),
        )
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

    override fun navigateToViewItFragment() {
        binding.bnvMain.selectedItemId = R.id.graph_view_it
    }

    private fun initBottomNaviSelectedListener(navController: NavController) {
        var previousTabId = R.id.graph_home
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.graph_home -> {
                    trackEvent(CLICK_HOME_BOTNAVI)
                    lifecycleScope.launch {
                        delay(100)
                        if (previousTabId == R.id.graph_profile) findHomeFragment()?.updateToLoadingState()
                    }
                }

                R.id.graph_news -> trackEvent(CLICK_NEWS_BOTNAVI)
                R.id.graph_profile -> trackEvent(CLICK_MYPROFILE_BOTNAVI)
            }
            lifecycleScope.launch {
                delay(200)
                previousTabId = it.itemId
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

    private fun updateStatusBarColor(navController: NavController) {
        // TODO : dark status bar 부분을 set에 넣어 주시면 됩니다!
        val darkStatusBarDestinations = setOf(
            com.teamwable.news.R.id.navigation_news_detail,
            com.teamwable.news.R.id.navigation_news,
            com.teamwable.community.R.id.navigation_community,
            viewitR.id.navigation_view_it,
            viewitR.id.navigation_view_it_posting,
            com.teamwable.ui.R.id.navigation_feed_image_dialog,
        )

        val noStatusBarChangeDestinations = setOf(
            com.teamwable.ui.R.id.navigation_two_button_dialog,
            com.teamwable.ui.R.id.navigation_bottomSheet,
            com.teamwable.ui.R.id.navigation_two_label_bottomsheet,
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in darkStatusBarDestinations -> {
                    binding.statusBarBackground.setStatusBarColor(com.teamwable.ui.R.color.black)
                    statusBarModeOf(false)
                }

                in noStatusBarChangeDestinations -> Unit

                else -> {
                    binding.statusBarBackground.setStatusBarColor(com.teamwable.ui.R.color.white)
                    statusBarModeOf()
                }
            }
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
