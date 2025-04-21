package cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.screens

import android.Manifest
import android.content.Context
import android.graphics.Paint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsNotFixed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels.MapViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


@Composable
fun MapScreen(
    viewModel: MapViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados observados
    val isRecording by viewModel.isRecording.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val routeLocations by viewModel.routeLocations.collectAsState()
    val totalDistance by viewModel.totalDistance.collectAsState()
    val cameraTracking by viewModel.cameraTracking.collectAsState()

    // Configuración de permisos
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            scope.launch {
                snackbarHostState.showSnackbar("Se requieren permisos de ubicación")
            }
        }
    }

    // Mapa OSM
    val mapView = remember {
        MapView(context).apply {
            Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            minZoomLevel = 12.0
            maxZoomLevel = 21.0
            controller.setZoom(18.0)
        }
    }

    // Marcador de posición actual
    val positionMarker = remember { Marker(mapView) }

    // Efecto para configurar el marcador
    DisposableEffect(Unit) {
        positionMarker.apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Mi posición"
            icon = context.getDrawable(android.R.drawable.ic_menu_mylocation)
        }
        mapView.overlays.add(positionMarker)

        onDispose { mapView.overlays.remove(positionMarker) }
    }

    // Efecto para actualizar posición y marcador
    LaunchedEffect(currentLocation) {
        currentLocation?.let { location ->
            val geoPoint = GeoPoint(location.latitude, location.longitude)

            // Actualizar marcador
            positionMarker.position = geoPoint
            mapView.overlays.removeIf { it is Marker && it != positionMarker }

            // Mover cámara si el seguimiento está activado
            if (cameraTracking) {
                mapView.controller.animateTo(geoPoint)
            }

            mapView.invalidate()
        }
    }

    // Efecto para dibujar la ruta
    LaunchedEffect(routeLocations) {
        if (routeLocations.size > 1) {
            val geoPoints = routeLocations.map { GeoPoint(it.latitude, it.longitude) }

            mapView.overlays.removeIf { it is Polyline }

            val routeLine = Polyline().apply {
                geoPoints.forEach { addPoint(it) }
                outlinePaint.strokeWidth = 10f
                outlinePaint.color = Color.Black.value.toInt()
                outlinePaint.strokeCap = Paint.Cap.ROUND
            }

            mapView.overlays.add(routeLine)
            mapView.invalidate()
        }
    }

    // Gestión de actualizaciones de ubicación
    DisposableEffect(Unit) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                viewModel.updateLocation(location)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        // Solicitar permisos
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        try {
            // Usar última ubicación conocida para respuesta rápida
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                viewModel.updateLocation(it)
            }

            // Solicitar actualizaciones continuas
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                5f,
                locationListener
            )
        } catch (e: SecurityException) {
            Log.e("MapScreen", "Error de permisos", e)
        }

        onDispose {
            locationManager.removeUpdates(locationListener)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Box {
                // Botón de seguimiento de cámara
                IconButton(
                    onClick = { viewModel.toggleCameraTracking() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(bottom = 70.dp)
                ) {
                    Icon(
                        imageVector = if (cameraTracking) Icons.Default.GpsFixed else Icons.Default.GpsNotFixed,
                        contentDescription = "Seguimiento de cámara"
                    )
                }

                // Botón de grabación de ruta
                FloatingActionButton(
                    onClick = { viewModel.toggleRecording() },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (isRecording) android.R.drawable.ic_media_pause
                            else android.R.drawable.ic_media_play
                        ),
                        contentDescription = if (isRecording) "Detener grabación" else "Iniciar grabación"
                    )
                }

                // Indicador de distancia
                if (isRecording) {
                    Text(
                        text = "Distancia: ${"%.2f".format(totalDistance)} m",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(bottom = 120.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}