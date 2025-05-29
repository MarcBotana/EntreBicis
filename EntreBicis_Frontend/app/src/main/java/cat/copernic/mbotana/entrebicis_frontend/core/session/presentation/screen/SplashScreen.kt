package cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.screen

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.session.model.SessionUser
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SplashViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(viewModel: SplashViewModel, navController: NavController, sessionViewModel: SessionViewModel) {
    val context = LocalContext.current

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION,)
    val hasLocationPermission = locationPermissionState.status.isGranted

    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    val userSession by sessionViewModel.userSession.collectAsState()

    val user by viewModel.user.collectAsState()
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(userSession, hasLocationPermission) {
        if (hasLocationPermission) {
            delay(750)
            if (userSession.isConnected) {
                viewModel.loadUserData(userSession.email)
            } else {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(isDataLoaded, hasLocationPermission) {
        if (isDataLoaded && hasLocationPermission) {
            val bottomNavIndex = "Map"
            sessionViewModel.updateSession(SessionUser(user!!.email, user!!.image, user!!.role, true))
            navController.navigate("main/$bottomNavIndex") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(backendException) {
        backendException?.let { toastMessage(context, it) }
    }

    LaunchedEffect(frontendException) {
        frontendException?.let { toastMessage(context, it) }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}