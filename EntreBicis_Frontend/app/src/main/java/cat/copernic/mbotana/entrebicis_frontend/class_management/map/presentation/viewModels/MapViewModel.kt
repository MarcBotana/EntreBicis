package cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel : ViewModel() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints

    private val _currentSpeed = MutableStateFlow<Float?>(null)
    val currentSpeed: StateFlow<Float?> = _currentSpeed

    private var trackingRoute = false

    private val _showStartDialog = MutableStateFlow(false)
    val showStartDialog: StateFlow<Boolean> = _showStartDialog

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    fun updateShowStartDialog(value: Boolean) {
        _showStartDialog.value = value
    }

    fun updateIsTracking(value: Boolean) {
        _isTracking.value = value
    }

    fun startTracking(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
                setMinUpdateIntervalMillis(1000)
            }.build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        val latLng = LatLng(location.latitude, location.longitude)
                        _currentLocation.value = latLng

                        Log.d("LocationDebug", "New Location: ${_currentLocation.value}")
                        _currentSpeed.value = location.speed * 3.6f // Convert m/s to km/h
                        Log.d("LocationDebug", "New Speed: ${_currentSpeed.value}")


                        if (trackingRoute) {
                            _routePoints.value += latLng
                        }
                    }
                }
            }

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback as LocationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun beginRoute() {
        _routePoints.value = emptyList()
        trackingRoute = true
    }

    fun stopRoute() {
        trackingRoute = false
    }

    fun stopTracking() {
        fusedLocationClient?.removeLocationUpdates(locationCallback ?: return)
    }
}