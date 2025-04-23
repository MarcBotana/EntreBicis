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
import cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoint.domain.models.GpsPoint
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.data.repositories.RouteRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.data.source.RouteApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteValidate
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
import java.time.LocalTime
import java.time.Duration
import java.util.Locale

class MapViewModel : ViewModel() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    private val _route = MutableStateFlow<Route?>(null)

    private val _gpsPoint = MutableStateFlow<List<GpsPoint>>(emptyList())

    private val _bearing = MutableStateFlow<Float?>(null)
    val bearing: StateFlow<Float?> = _bearing

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _currentSpeed = MutableStateFlow(0f)
    val currentSpeed: StateFlow<Float> = _currentSpeed

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints

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

    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

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

            val gpsPoint = GpsPoint(
                null,
                latitude = latLng.latitude,
                longitude = latLng.longitude,
                time = LocalTime.now().toString(),
                speed = _currentSpeed.value,
                isValid = true,
                route = null
            )

            _gpsPoint.value += gpsPoint

        } else if (_routePoints.value.size > 1) {
            _startRoutePoint.value = _routePoints.value.first()
            _endRoutePoint.value = _routePoints.value.last()
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

    private val routeApi: RouteApiRest = RouteRetrofitInstance.retrofitInstance.create(
        RouteApiRest::class.java
    )


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveRoute(email: String) {
        viewModelScope.launch {
            try {
                updateRouteData()
                if (_route.value != null) {
                    val response = routeApi.createRoute(
                        email,
                        route = _route.value!!,
                    )
                    if (response.isSuccessful) {
                        Log.e("MapViewModel", "ROUTE_SAVE_SUCCESS!")
                    } else if (response.code() == 500) {
                        Log.e("MapViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
                        _backendException.value = "Error amb el servidor!"
                    }
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateRouteData() {
        _route.value = Route(
            id = null,
            routeValidate = RouteValidate.NOT_VALIDATED,
            totalRoutePoints = null,
            totalRouteDistance = calculateRouteDistance(),
            totalRouteTime = null,
            maxRouteVelocity = _gpsPoint.value.maxOfOrNull { it.speed.toDouble() } ?: 0.0,
            avgRouteVelocity = _gpsPoint.value.map { it.speed }.average().toFloat().toDouble(),
            gpsPoints = _gpsPoint.value,
            user = null
        )
    }

    private fun calculateRouteDistance(): Double {
        var totalDistance = 0.0

        for (i in 0 until _gpsPoint.value.size - 1) {
            val locationA = _gpsPoint.value[i]
            val locationB = _gpsPoint.value[i + 1]

            val pointA = Location("").apply {
                latitude = locationA.latitude
                longitude = locationA.longitude
            }

            val pointB = Location("").apply {
                latitude = locationB.latitude
                longitude = locationB.longitude
            }
            totalDistance += pointA.distanceTo(pointB)
        }
        return totalDistance / 1000.0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateRouteTime(): String {

        val startTime = LocalTime.parse(_gpsPoint.value.first().time)
        val finishTime = LocalTime.parse(_gpsPoint.value.last().time)

        val duration = Duration.between(startTime, finishTime)

        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            duration.toHours(),
            duration.toMinutes() % 60,
            duration.seconds % 60
        )
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
