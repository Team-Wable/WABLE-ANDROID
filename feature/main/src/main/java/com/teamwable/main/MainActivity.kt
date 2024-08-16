package com.teamwable.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.teamwable.main.databinding.ActivityMainBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController

        //TODO : 나중에 BADGE 보이게 하는 로직으로 이동
        setBadgeOnNotification(true)

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

    private fun handleBottomNavigationVisibility(destination: NavDestination) {
        if (destination.id in listOf(
                com.teamwable.home.R.id.navigation_home,
                com.teamwable.news.R.id.navigation_news,
                com.teamwable.notification.R.id.navigation_notification,
                com.teamwable.profile.R.id.navigation_profile,
            )
        ) {
            binding.groupMainBnv.visible(true)
        } else {
            binding.groupMainBnv.visible(false)
        }
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
