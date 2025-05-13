package cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.data.repositories.RouteRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.data.source.RouteApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.data.repositories.SystemParamsRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.data.source.SystemParamsApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.domain.models.SystemParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.create

class RouteViewModel: ViewModel() {

    //Variable Preparation
    private val _routeList = MutableStateFlow<List<Route>?>(emptyList())
    val routeList: StateFlow<List<Route>?> = _routeList

    private val _routeDetail = MutableStateFlow<Route?>(null)
    val routeDetail: StateFlow<Route?> = _routeDetail

    private val _systemParams = MutableStateFlow<SystemParams?>(null)
    val systemParams: StateFlow<SystemParams?> = _systemParams

    //Error Messages
    private val _routeNotFoundError = MutableStateFlow<String?>(null)
    val routeNotFoundError: StateFlow<String?> = _routeNotFoundError

    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val routeApi: RouteApiRest =
        RouteRetrofitInstance.retrofitInstance.create(
            RouteApiRest::class.java
        )

    private val systemParamsApi: SystemParamsApiRest =
        SystemParamsRetrofitInstance.retrofitInstance.create(
            SystemParamsApiRest::class.java
        )

    fun loadData(email: String) {
        viewModelScope.launch {
            try {
                val response1 = routeApi.getUserRoutesList(email)
                val response2 = systemParamsApi.getSystemParamsById(1L)
                if (response1.isSuccessful) {
                    Log.d("RouteViewModel", "USER ROUTES ACQUIRED SUCCESS")
                    _routeList.value = response1.body()
                } else if (response1.code() == 500) {
                    Log.e(
                        "RouteViewModel",
                        "BACKEND EXCEPTION: ${response1.errorBody()?.string()}"
                    )
                    _backendException.value = "Error amb el servidor!"
                }
                if (response2.isSuccessful) {
                    Log.d("RouteViewModel", "SYSTEM PARAMS ACQUIRED SUCCESS")
                    _systemParams.value = response2.body()
                } else if (response2.code() == 500) {
                    Log.e(
                        "RouteViewModel",
                        "BACKEND EXCEPTION: ${response1.errorBody()?.string()}"
                    )
                    _backendException.value = "Error amb el servidor!"
                }
            } catch (e: Exception) {
                Log.e("RouteViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    fun loadRouteDetail(id: Long) {
        viewModelScope.launch {
            try {
                val response = routeApi.getRouteDetail(id)
                if (response.isSuccessful) {
                    Log.d("RouteViewModel", "ROUTE ACQUIRED SUCCESS")
                    _routeDetail.value = response.body()
                } else if (response.code() == 404) {
                    Log.e("RouteViewModel", "ROUTE_NOT_FOUND!")
                    _routeNotFoundError.value = "No s'ha trobat la ruta!"
                } else if (response.code() == 500) {
                    Log.e(
                        "RouteViewModel",
                        "BACKEND EXCEPTION: ${response.errorBody()?.string()}"
                    )
                    _backendException.value = "Error amb el servidor!"
                }
            } catch (e: Exception) {
                Log.e("RouteViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

}