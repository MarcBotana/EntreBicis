package cat.copernic.mbotana.entrebicis_frontend.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.screens.MapScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels.MapViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.options.presentation.screens.OptionsScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.screens.ReservationDetail
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.screens.ReservationsScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.viewModels.ReservationViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens.RewardDetail
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens.RewardsScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.viewModels.RewardsViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens.ChangePasswordScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens.LoginScreen
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewModels.ChangePasswordViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewModels.LoginViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.screen.SplashScreen
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SplashViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("splash") {
            val splashViewModel: SplashViewModel = viewModel()
            SplashScreen(splashViewModel, navController, sessionViewModel)
        }

        composable("login") {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(loginViewModel, sessionViewModel, navController)
        }
        composable("changePassword") {
            val changePasswordViewModel: ChangePasswordViewModel = viewModel()
            ChangePasswordScreen(changePasswordViewModel, navController)
        }

        composable("main/{bottomNavIndex}") { backStackEntry ->
            val bottomNavIndex = backStackEntry.arguments?.getString("bottomNavIndex")
            val reservationViewModel: ReservationViewModel = viewModel()
            val rewardsViewModel: RewardsViewModel = viewModel()
            val mapViewModel: MapViewModel = viewModel()

            MainScreen(
                reservationViewModel,
                rewardsViewModel,
                mapViewModel,
                sessionViewModel,
                navController,
                bottomNavIndex ?: ""
            )
        }

        composable("rewardDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLong()
            val rewardsViewModel: RewardsViewModel = viewModel()
            RewardDetail(rewardsViewModel, sessionViewModel, navController, id ?: -1L)
        }

        composable("reservationDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLong()
            val reservationViewModel: ReservationViewModel = viewModel()
            ReservationDetail(reservationViewModel, sessionViewModel, navController, id ?: -1L)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    reservationViewModel: ReservationViewModel,
    rewardsViewModel: RewardsViewModel,
    mapViewModel: MapViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    bottomNavIndex: String
) {

    val navHostController = rememberNavController()
    val userSession by sessionViewModel.userSession.collectAsState()
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val isTrackingRoute by mapViewModel.isTrackingRoute.collectAsState()

    val startDestination = when (bottomNavIndex) {
        "Res" -> BottomNavItem.Res.route
        "Rec" -> BottomNavItem.Rec.route
        "Map" -> BottomNavItem.Map.route
        "Rou" -> BottomNavItem.Rou.route
        "Opt" -> BottomNavItem.Opt.route
        else -> BottomNavItem.Map.route
    }

    val screenTitle = when (currentRoute) {
        BottomNavItem.Res.route -> "Reserves"
        BottomNavItem.Rec.route -> "Recompenses"
        BottomNavItem.Map.route -> "Mapa"
        BottomNavItem.Rou.route -> "Rutes"
        BottomNavItem.Opt.route -> "Opcions"
        else -> ""
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar =
        { CustomTopBar(screenTitle, userSession, true) },
        bottomBar = {
            BottomNavigationBar(
                navController = navHostController,
                isTrackingRoute = isTrackingRoute
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(BottomNavItem.Res.route) {
                ReservationsScreen(reservationViewModel, sessionViewModel, navController)
            }
            composable(BottomNavItem.Rec.route) {
                RewardsScreen(rewardsViewModel, navController)
            }
            composable(BottomNavItem.Map.route) {
                MapScreen(mapViewModel, sessionViewModel, navController)
            }
            composable(BottomNavItem.Rou.route) {
                //RoutesScreen(MapViewModel(), sessionViewModel, navController)
            }
            composable(BottomNavItem.Opt.route) {
                OptionsScreen(sessionViewModel, navController)
            }
        }
    }
}




