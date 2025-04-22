package cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _currentSpeed = MutableStateFlow(0f)
    val currentSpeed: StateFlow<Float> = _currentSpeed

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints

    private val _showStartDialog = MutableStateFlow(false)
    val showStartDialog: StateFlow<Boolean> = _showStartDialog

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private fun updateLocation(location: Location) {
            val latLng = LatLng(location.latitude, location.longitude)
            _currentLocation.value = latLng
            _currentSpeed.value = location.speed * 3.6f

            Log.d("LocationDebug", "New Location: ${_currentLocation.value}")
            Log.d("LocationDebug", "New Speed: ${_currentSpeed.value}")
            Log.d("LocationDebug", "IsTracking: ${_isTracking.value}")


            if (_isTracking.value) {
                _routePoints.value += latLng
                Log.d("LocationDebug", "New Point: ${routePoints.value.size}")

            }


    }

    fun updateShowStartDialog(value: Boolean) {
        _showStartDialog.value = value
    }

    fun updateIsTracking(value: Boolean) {
        _isTracking.value = value
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
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { updateLocation(it) }
                }
            }

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )

            Log.d("LocationDebug", "Requesting location updates...")
        }

    }

    fun beginRoute(value: Boolean) {
        _routePoints.value = emptyList()
        _isTracking.value = value
        Log.d("Route", "BeginRoute: ${_isTracking.value}")

    }

    fun stopRoute() {
        _isTracking.value = false
    }

    fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
            Log.d("LocationDebug", "Stopped location updates")
        }
    }
}
