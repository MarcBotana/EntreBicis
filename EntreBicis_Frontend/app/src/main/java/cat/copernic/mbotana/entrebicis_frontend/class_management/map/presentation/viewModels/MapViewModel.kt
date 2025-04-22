package cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoints.domain.models.GpsPoints
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MapViewModel : ViewModel() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    private val _route = MutableStateFlow<Route?>(null)

    private val _bearing = MutableStateFlow<Float?>(null)
    val bearing: StateFlow<Float?> = _bearing

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _currentSpeed = MutableStateFlow(0f)
    val currentSpeed: StateFlow<Float> = _currentSpeed

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints

    private val _gpsPoints = MutableStateFlow<List<GpsPoints>>(emptyList())

    private val _showStartDialog = MutableStateFlow(false)
    val showStartDialog: StateFlow<Boolean> = _showStartDialog

    private val _showEndDialog = MutableStateFlow(false)
    val showEndDialog: StateFlow<Boolean> = _showEndDialog

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private val _startRoutePoint = MutableStateFlow<LatLng?>(null)
    val startRoutePoint: StateFlow<LatLng?> = _startRoutePoint

    private val _endRoutePoint = MutableStateFlow<LatLng?>(null)
    val endRoutePoint: StateFlow<LatLng?> = _endRoutePoint

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        _currentLocation.value = latLng
        _currentSpeed.value = location.speed * 3.6f
        _bearing.value = location.bearing

        Log.d("LocationDebug", "New Location: ${_currentLocation.value}")
        Log.d("LocationDebug", "New Speed: ${_currentSpeed.value}")


        if (_isTracking.value) {
            _routePoints.value += latLng

            val gpsPoints: GpsPoints = GpsPoints(
                null,
                latitude = latLng.latitude,
                longitude = latLng.longitude,
                time = LocalDateTime.now().toString(),
                speed = _currentSpeed.value,
                route = null
            )

            _gpsPoints.value += gpsPoints

        } else if (_routePoints.value.size > 1) {
            _startRoutePoint.value = _routePoints.value[0]
            _endRoutePoint.value = _routePoints.value[_routePoints.value.size - 1]
        }
    }

    fun updateShowStartDialog(value: Boolean) {
        _showStartDialog.value = value
    }

    fun updateShowEndDialog(value: Boolean) {
        _showEndDialog.value = value
    }


    suspend fun centerCamera(cameraPositionState: CameraPositionState) {
        if (_isTracking.value) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(_currentLocation.value!!)
                        .zoom(20f)
                        .bearing(_bearing.value!!)
                        .tilt(45f)
                        .build()
                )
            )
        } else {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(_currentLocation.value!!)
                        .zoom(18f)
                        .build()
                )
            )
        }
    }

    fun startTracking(context: Context) {
        viewModelScope.launch {
            Log.d("LocationDebug", "startTracking called")

            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w("LocationDebug", "Location permission NOT granted")
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 500
            ).apply {
                setMinUpdateIntervalMillis(1000)
            }.build()

            locationCallback = object : LocationCallback() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { updateLocation(it) }
                }
            }

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        }
    }

    fun generateRoute(userEmail: String) {
        viewModelScope.launch {



        }

    }

    fun beginRoute() {
        _isTracking.value = true
        _routePoints.value = emptyList()
        _startRoutePoint.value = null
        _endRoutePoint.value = null
    }

    fun stopRoute() {
        _isTracking.value = false
    }

    fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
        }
    }
}
