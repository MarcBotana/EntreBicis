package cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.screens

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels.MapViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun MapScreen(
    viewModel: MapViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPositionState = rememberCameraPositionState()
    var mapLoaded by remember { mutableStateOf(false) }

    val currentLocation by viewModel.currentLocation.collectAsState()
    val routePoints by viewModel.routePoints.collectAsState()
    val currentSpeed by viewModel.currentSpeed.collectAsState()

    val startRoutePoint by viewModel.startRoutePoint.collectAsState()
    val endRoutePoint by viewModel.endRoutePoint.collectAsState()

    val userSession by sessionViewModel.userSession.collectAsState()

    val showStartDialog by viewModel.showStartDialog.collectAsState()
    val showEndDialog by viewModel.showEndDialog.collectAsState()
    val showSaveDialog by viewModel.showSaveDialog.collectAsState()
    val showStopTimeDialog by viewModel.showStopTimeDialog.collectAsState()

    val isTrackingRoute by viewModel.isTrackingRoute.collectAsState()

    val isTrackingPosition by viewModel.isTrackingPosition.collectAsState()

    LaunchedEffect(mapLoaded) {
        if (locationPermissionState.status.isGranted && mapLoaded) {
            viewModel.startTracking(context)
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(mapLoaded, currentLocation) {
        if (mapLoaded && currentLocation != null) {
            viewModel.centerCamera(cameraPositionState)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopTracking()
            viewModel.clearRoute()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = MapProperties(isMyLocationEnabled = true),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    mapLoaded = true
                },
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true,
                    compassEnabled = true,
                    scrollGesturesEnabled = true,
                    zoomGesturesEnabled = true,
                    tiltGesturesEnabled = true
                )
            ) {

                if (isTrackingRoute && routePoints.size > 1) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 50f,
                        startCap = RoundCap(),
                        endCap = RoundCap()
                    )
                } else if (routePoints.size > 1) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 25f,
                        startCap = RoundCap(),
                        endCap = RoundCap()
                    )
                }

                if (startRoutePoint != null) {
                    Marker(
                        state = MarkerState(position = startRoutePoint!!),
                        title = "Inici Ruta"
                    )
                }

                if (endRoutePoint != null) {
                    Marker(
                        state = MarkerState(position = endRoutePoint!!),
                        title = "Final Ruta"
                    )
                }
            }

            if (!isTrackingPosition) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Obtenint posició...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(90.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .border(6.dp, Color.Red, CircleShape)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "%.1f".format(currentSpeed),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "km/h",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        if (isTrackingPosition) {
                            if (!isTrackingRoute) {
                                viewModel.updateShowStartDialog(true)
                            } else {
                                viewModel.updateShowEndDialog(true)
                            }
                        }
                    },
                    modifier = Modifier.alpha(if (isTrackingPosition) 1f else 0.5f)
                ) {
                    Row(modifier = Modifier.padding(6.dp)) {
                        Icon(
                            imageVector = if (isTrackingRoute) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isTrackingRoute) "Stop Route" else "Start Route"
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            modifier = Modifier.padding(top = 2.dp),
                            text = if (isTrackingRoute) "Finalitzar Ruta" else "Començar Ruta"
                        )
                    }
                }
            }
        }
    }


    if (showStartDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowStartDialog(false) },
            title = { Text("Iniciar ruta") },
            text = { Text("Vols començar a registrar una nova ruta?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearRoute()
                    viewModel.beginRoute()
                    viewModel.updateShowStartDialog(false)
                }) {
                    Text("Començar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.updateShowStartDialog(false)
                }) {
                    Text("Cancel·lar")
                }
            }
        )
    }

    if (showEndDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowEndDialog(false) },
            title = { Text("Finalitzar ruta") },
            text = { Text("Vols finalitzar aquesta ruta?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.stopRoute()
                    viewModel.updateShowEndDialog(false)
                    viewModel.updateShowSaveDialog(true)
                }) {
                    Text("Finalitzar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.updateShowEndDialog(false)
                }) {
                    Text("Cancel·lar")
                }
            }
        )
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowEndDialog(false) },
            text = { Text("Guardar Ruta?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveRoute(userSession.email)
                    viewModel.updateShowSaveDialog(false)
                }) {
                    Text("Si, guardar.")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.updateShowSaveDialog(false)
                }) {
                    Text("No")
                }
            }
        )
    }

    if (showStopTimeDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowEndDialog(false) },
            title = { Text("Estas aturat!") },
            text = { Text("Has passat massa temps aturat, la ruta ha estat finalitzada y guardada automàticament!") },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = {
                    viewModel.saveRoute(userSession.email)
                    viewModel.updateShowStopTimeDialog(false)
                }) {
                    Text("D'acord")
                }
            }
        )
    }
}
