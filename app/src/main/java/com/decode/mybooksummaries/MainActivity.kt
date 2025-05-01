package com.decode.mybooksummaries

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.core.ui.theme.MyBookSummariesTheme
import com.decode.mybooksummaries.navigation.NavGraph
import com.decode.mybooksummaries.presentation.onboarding.OnboardingUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val onboardingUtils by lazy { OnboardingUtils(this) }
    private val mainViewModel by viewModels<MainViewModel>()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("MainActivity", "Bildirim izni verildi.")
        } else {
            Log.d("MainActivity", "Bildirim izni verilmedi.")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            MyBookSummariesTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = CustomTheme.colors.backgroundColor
                ) { innerPadding->
                    NavGraph(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        navController = navController,
                        onboardingUtils = onboardingUtils,
                        isLoggedIn = mainViewModel.authUser
                    )
                }
            }
        }
        requestNotificationPermissionIfNeeded()
    }
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}


















