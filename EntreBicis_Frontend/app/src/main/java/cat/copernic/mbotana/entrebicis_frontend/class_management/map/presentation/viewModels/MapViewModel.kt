package cat.copernic.mbotana.entrebicis_frontend.class_management.map.presentation.viewModels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    // Estado de grabación
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    // Ubicación actual del usuario
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    // Lista de ubicaciones de la ruta
    private val _routeLocations = MutableStateFlow<List<Location>>(emptyList())
    val routeLocations: StateFlow<List<Location>> = _routeLocations.asStateFlow()

    // Distancia total
    private val _totalDistance = MutableStateFlow(0f)
    val totalDistance: StateFlow<Float> = _totalDistance.asStateFlow()

    // Modo seguimiento de cámara
    private val _cameraTracking = MutableStateFlow(true)
    val cameraTracking: StateFlow<Boolean> = _cameraTracking.asStateFlow()

    fun toggleRecording() {
        _isRecording.value = !_isRecording.value
        if (!_isRecording.value) {
            _routeLocations.value = emptyList()
            _totalDistance.value = 0f
        }
    }

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            _currentLocation.value = location

            if (_isRecording.value) {
                val currentLocations = _routeLocations.value
                if (currentLocations.isNotEmpty()) {
                    _totalDistance.value += location.distanceTo(currentLocations.last())
                }
                _routeLocations.value = currentLocations + location
            }
        }
    }

    fun toggleCameraTracking() {
        _cameraTracking.value = !_cameraTracking.value
    }
}