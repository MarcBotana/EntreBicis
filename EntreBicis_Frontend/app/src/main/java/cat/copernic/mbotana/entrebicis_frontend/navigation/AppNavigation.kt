package cat.copernic.mbotana.entrebicis_frontend.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.screens.MapScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewmodels.MapViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.options.presentation.screens.OptionsScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens.RewardsScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.viewmodel.RewardsViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens.ChangePasswordScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens.LoginScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewmodels.ChangePasswordViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewmodels.LoginViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.screen.SplashScreen
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel


@Composable
fun AppNavigation(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("splash") { SplashScreen(navController, sessionViewModel) }

        composable("login") { LoginScreen(LoginViewModel(), sessionViewModel, navController) }
        composable("changePassword") { ChangePasswordScreen(ChangePasswordViewModel(), navController) }

        composable("main/{bottomNavIndex}") { backStackEntry ->
            val bottomNavIndex = backStackEntry.arguments?.getString("bottomNavIndex")
            MainScreen(sessionViewModel, navController, bottomNavIndex ?: "")
        }
    }
}

@Composable
fun MainScreen(sessionViewModel: SessionViewModel, navController: NavController, bottomNavIndex: String) {

    val navHostController = rememberNavController()
    val userSession by sessionViewModel.userSession.collectAsState()
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val startDestination = when (bottomNavIndex) {
        "M" -> BottomNavItem.Map.route
        "R" -> BottomNavItem.Rec.route
        "O" -> BottomNavItem.Opt.route
        else -> BottomNavItem.Map.route
    }

    val screenTitle =  when (currentRoute) {
        BottomNavItem.Map.route -> "Mapa"
        BottomNavItem.Rec.route -> "Recompenses"
        BottomNavItem.Opt.route -> "Menu"
        else -> ""
    }

    Scaffold(
        topBar =
        { CustomTopBar(screenTitle, userSession.totalPoints, true) },
        bottomBar = {
            BottomNavigationBar(navController = navHostController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Map.route) {
                MapScreen(MapViewModel(), sessionViewModel, navController)
            }
            composable(BottomNavItem.Rec.route) {
                RewardsScreen(RewardsViewModel(), navController)
            }
            composable(BottomNavItem.Opt.route) {
                OptionsScreen(sessionViewModel, navController)
            }
        }
    }
}



