package com.example.neurableble

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {

    enum class Screens() {
        Home,
        Focus,
        BLE
    }

    sealed class NavItem(val route: String) {
        data object Home: NavItem(Screens.Home.name)
        data object Focus: NavItem(Screens.Focus.name)
        data object BLE: NavItem(Screens.BLE.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppNavHost(navController = rememberNavController())
        }
    }

    @Composable
    fun AppNavHost(navController: NavHostController) {
        NavHost(navController, startDestination = Screens.Home.name) {
            composable(NavItem.Home.route) { HomeScreen(navController = navController) }
            composable(NavItem.Focus.route) { FocusScreen(navController = navController) }
            composable(NavItem.BLE.route) {
                // Request Bluetooth permissions when navigating to BLEScreen
                RequestPermissions()
                BLEScreen(navController = navController)
            }
        }
    }

    @Composable
    fun RequestPermissions() {
        val context = LocalContext.current
        val viewModel = viewModel<BLEViewModel>()

        val permissions = viewModel.neededPermissions()

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { }

        LaunchedEffect(key1 = true) {
            val allPermissionsGranted = permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            if (!allPermissionsGranted) {
                launcher.launch(permissions)
            }
        }
    }
}