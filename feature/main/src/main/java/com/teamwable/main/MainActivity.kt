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
            supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
        val navController = navHostFragment.navController

        //TODO : 나중에 BADGE 보이게 하는 로직으로 이동
        setBadgeOnNotification(navController, true)

        with(binding) {
            bnvMain.itemIconTintList = null
            bnvMain.setupWithNavController(navController)
        }
        initBottomNavigationChangedListener(navController)
    }

    private fun initBottomNavigationChangedListener(navController: NavController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            handleBottomNavigationVisibility(navController, destination)
            if (destination.id == R.id.navigation_notification) setBadgeOnNotification(navController, false)
        }
    }

    private fun handleBottomNavigationVisibility(navController: NavController, destination: NavDestination) {
        when (destination.id) {
            R.id.navigation_posting -> {
                binding.groupMainBnv.visible(false)
            }

            else -> {
                binding.groupMainBnv.visible(true)
            }
        }
    }

    private fun setBadgeOnNotification(navController: NavController, isVisible: Boolean) {
        binding.bnvMain.getOrCreateBadge(R.id.navigation_notification).apply {
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
