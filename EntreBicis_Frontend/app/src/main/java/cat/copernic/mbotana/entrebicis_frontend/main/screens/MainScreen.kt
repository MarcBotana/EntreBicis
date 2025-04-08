package cat.copernic.mbotana.entrebicis_frontend.main.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.screens.MapScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewmodels.MapViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.options.presentation.screens.OptionsScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens.RewardsScreen
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import cat.copernic.mbotana.entrebicis_frontend.navigation.BottomNavItem
import cat.copernic.mbotana.entrebicis_frontend.navigation.BottomNavigationBar


@Composable
fun MainScreen(sessionViewModel: SessionViewModel, navController: NavController, bottomNavIndex: String) {

    val navHostController = rememberNavController()

    val startDestination = when (bottomNavIndex) {
        "M" -> BottomNavItem.Map.route
        "R" -> BottomNavItem.Rec.route
        "O" -> BottomNavItem.Opt.route
        else -> BottomNavItem.Map.route
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navHostController)
        }
    ) { paddingValues ->
        // Este NavHost se encarga de la navegación interna dentro de MainScreen
        NavHost(
            navController = navHostController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Map.route) {
                MapScreen(MapViewModel(), sessionViewModel, navController)
            }
            composable(BottomNavItem.Rec.route) {
                RewardsScreen(sessionViewModel, navController)  // Este se mantiene en la misma jerarquía
            }
            composable(BottomNavItem.Opt.route) {
                OptionsScreen(sessionViewModel, navController)
            }
        }
    }
}
