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
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.data.repositories.SystemParamsRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.data.source.SystemParamsApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.domain.models.SystemParams
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale

class MapViewModel : ViewModel() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    private val _route = MutableStateFlow<Route?>(null)

    private val _gpsPoint = MutableStateFlow<List<GpsPoint>>(emptyList())

    private val _systemParams = MutableStateFlow<SystemParams?>(null)

    private val _bearing = MutableStateFlow<Float?>(null)

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _currentSpeed = MutableStateFlow(0f)
    val currentSpeed: StateFlow<Float> = _currentSpeed

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints

    private val _routeTime = MutableStateFlow<List<LocalTime>>(emptyList())

    private val _showStartDialog = MutableStateFlow(false)
    val showStartDialog: StateFlow<Boolean> = _showStartDialog

    private val _showEndDialog = MutableStateFlow(false)
    val showEndDialog: StateFlow<Boolean> = _showEndDialog

    private val _showSaveDialog = MutableStateFlow(false)
    val showSaveDialog: StateFlow<Boolean> = _showSaveDialog

    private val _showStopTimeDialog = MutableStateFlow(false)
    val showStopTimeDialog: StateFlow<Boolean> = _showStopTimeDialog

    private val _isTrackingRoute = MutableStateFlow(false)
    val isTrackingRoute: StateFlow<Boolean> = _isTrackingRoute

    private val _isTrackingPosition = MutableStateFlow(false)
    val isTrackingPosition: StateFlow<Boolean> = _isTrackingPosition

    private val _startRoutePoint = MutableStateFlow<LatLng?>(null)
    val startRoutePoint: StateFlow<LatLng?> = _startRoutePoint

    private val _endRoutePoint = MutableStateFlow<LatLng?>(null)
    val endRoutePoint: StateFlow<LatLng?> = _endRoutePoint

    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val routeApi: RouteApiRest = RouteRetrofitInstance.retrofitInstance.create(
        RouteApiRest::class.java
    )

    private val systemParamsApi: SystemParamsApiRest =
        SystemParamsRetrofitInstance.retrofitInstance.create(
            SystemParamsApiRest::class.java
        )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        _currentLocation.value = latLng
        _currentSpeed.value = location.speed * 3.6f
        _bearing.value = location.bearing

        Log.d("LocationDebug", "New Location: ${_currentLocation.value}")
        Log.d("LocationDebug", "New Speed: ${_currentSpeed.value}")

        if (_isTrackingRoute.value) {
            _routePoints.value += latLng

            _startRoutePoint.value = _routePoints.value.first()

            if (!hasBeenStopped()) {
                val gpsPoint = GpsPoint(
                    null,
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    time = LocalTime.now().toString(),
                    speed = _currentSpeed.value,
                    route = null
                )
                _gpsPoint.value += gpsPoint
            } else {
                stopRoute()
                _showStopTimeDialog.value = true
                _routeTime.value = emptyList()
                _gpsPoint.value = emptyList()
            }
        } else if (_routePoints.value.size > 1) {
            _endRoutePoint.value = _routePoints.value.last()
        }
    }

    fun updateShowStartDialog(value: Boolean) {
        _showStartDialog.value = value
    }

    fun updateShowEndDialog(value: Boolean) {
        _showEndDialog.value = value
    }

    fun updateShowStopTimeDialog(value: Boolean) {
        _showStopTimeDialog.value = value
    }

    fun updateShowSaveDialog(value: Boolean) {
        _showSaveDialog.value = value
    }

    suspend fun centerCamera(cameraPositionState: CameraPositionState) {
        if (_isTrackingRoute.value) {
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
        } else if (_routePoints.value.size > 1) {
            val latitudes = _routePoints.value.map { it.latitude }
            val longitudes = _routePoints.value.map { it.longitude }

            val minLat = latitudes.minOrNull() ?: 0.0
            val maxLat = latitudes.maxOrNull() ?: 0.0
            val minLng = longitudes.minOrNull() ?: 0.0
            val maxLng = longitudes.maxOrNull() ?: 0.0

            val bounds = LatLngBounds(
                LatLng(minLat, minLng),
                LatLng(maxLat, maxLng)
            )

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    200
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

            var systemParamsReady = false

            try {
                val response = systemParamsApi.getSystemParamsById(1)
                if (response.isSuccessful) {
                    _systemParams.value = response.body()
                    systemParamsReady = true
                } else if (response.code() == 404) {
                    Log.e("MapViewModel", "SYSTEM_PARAMS_NOT_FOUND!")
                    _frontendException.value = "Error amb el client!"
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }

            if (systemParamsReady) {
                if (ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.w("LocationDebug", "Location permission NOT granted")
                }

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 250
                ).apply {
                    setMinUpdateIntervalMillis(250)
                }.build()

                locationCallback = object : LocationCallback() {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onLocationResult(result: LocationResult) {
                        result.lastLocation?.let { updateLocation(it) }
                        _isTrackingPosition.value = true
                    }
                }

                fusedLocationClient?.requestLocationUpdates(
                    locationRequest,
                    locationCallback!!,
                    Looper.getMainLooper()
                )
            }
        }
    }


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
                        Log.e(
                            "MapViewModel",
                            "BACKEND EXCEPTION: ${response.errorBody()?.string()}"
                        )
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
            routeState = RouteState.PENDING,
            routeDate = LocalDateTime.now(ZoneId.of("UTC+2")).toString(),
            totalRoutePoints = null,
            totalRouteDistance = calculateRouteDistance(),
            totalRouteTime = calculateRouteTime(),
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
            Locale.US,
            "%02d:%02d:%02d",
            duration.toHours(),
            duration.toMinutes() % 60,
            duration.seconds % 60
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hasBeenStopped(): Boolean {
        val currentSpeed = _currentSpeed.value
        val velInterval = 0.5f
        val stopTime = _systemParams.value?.stopMaxTime?.toLong()

        var isStopped = false

        if (currentSpeed < velInterval) {
            _routeTime.value += LocalTime.now()
            val firstTime = _routeTime.value.first()
            val lastTime = _routeTime.value.last()

            if (Duration.between(firstTime, lastTime).toMinutes() >= stopTime!!) {
                isStopped = true
            }
        } else {
            _routeTime.value = emptyList()
        }

        return isStopped
    }


    fun beginRoute() {
        _isTrackingRoute.value = true
    }

    fun stopRoute() {
        _isTrackingRoute.value = false
    }

    fun clearRoute() {
        _routePoints.value = emptyList()
        _gpsPoint.value = emptyList()
        _startRoutePoint.value = null
        _endRoutePoint.value = null
    }

    fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
            _isTrackingPosition.value = false
        }

    }
}
