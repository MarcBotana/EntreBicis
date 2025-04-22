package cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels.MapViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
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

    Log.d("UIUpdate", "Recomposing with location = $currentLocation and speed = $currentSpeed")


    val userSession by sessionViewModel.userSession.collectAsState()

    val showStartDialog by viewModel.showStartDialog.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()

    LaunchedEffect(mapLoaded) {
        if (locationPermissionState.status.isGranted) {
            viewModel.startTracking(context)
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(mapLoaded, currentLocation) {
        if (mapLoaded && currentLocation != null) {
            Log.d("LocationCamera", "Centrando la cámara en: $currentLocation")
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 18f))
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopTracking()
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

                if (routePoints.size > 1) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 20f
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ubicació: ${currentLocation?.let { "%.2f".format(it.latitude) }} - ${currentLocation?.let {
                        "%.2f".format(
                            it.longitude)
                    }}",
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Velocitat: ${"%.1f".format(currentSpeed)} km/h",
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center

                )
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                FloatingActionButton(
                    onClick = {
                        if (!isTracking) {
                            viewModel.updateShowStartDialog(true)
                        } else {
                            viewModel.updateIsTracking(false)
                            viewModel.stopRoute()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isTracking) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (isTracking) "Stop Route" else "Start Route"
                    )
                }
            }
        }

    }




    if (showStartDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowStartDialog(false) },
            title = { Text("Começar ruta") },
            text = { Text("Vols començar a registrar una nova ruta?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.beginRoute(true)
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
}

fun createCustomMarker(): BitmapDescriptor {
    val width = 100
    val height = 120

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    val paint = Paint().apply {
        color = android.graphics.Color.BLUE
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    val path = Path().apply {
        moveTo(width / 2f, height.toFloat())
        lineTo(0f, 0f)
        lineTo(width.toFloat(), 0f)
        close()
    }

    canvas.drawPath(path, paint)
    paint.color = android.graphics.Color.WHITE
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 8f
    canvas.drawPath(path, paint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}