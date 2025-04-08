package cat.copernic.mbotana.entrebicis_frontend.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens.LoginScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewmodels.LoginViewModel
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

        composable("main/{bottomNavIndex}") { backStackEntry ->
            val bottomNavIndex = backStackEntry.arguments?.getString("bottomNavIndex")
            MainScreen(sessionViewModel, navController, bottomNavIndex ?: "")
        }
    }
}



