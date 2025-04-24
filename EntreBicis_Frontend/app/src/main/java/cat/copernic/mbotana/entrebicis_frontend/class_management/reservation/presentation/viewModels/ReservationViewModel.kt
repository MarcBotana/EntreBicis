package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.repositories.ReservationRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.data.repositories.RewardRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.source.remote.ReservationApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.domain.models.Reservation
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.data.source.remote.RewardApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.UserRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.UserApiRest
import cat.copernic.mbotana.entrebicis_frontend.core.session.model.SessionUser
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservationViewModel(userEmail: String) : ViewModel() {

    //Variable Preparation
    private val _reservationList = MutableStateFlow<List<Reservation>?>(emptyList())
    val reservationList: StateFlow<List<Reservation>?> = _reservationList

    private val _reservationDetail = MutableStateFlow<Reservation?>(null)
    val reservationDetail: StateFlow<Reservation?> = _reservationDetail

    //Error Messages
    private val _reservationNotFoundError = MutableStateFlow<String?>(null)
    val reservationNotFoundError: StateFlow<String?> = _reservationNotFoundError

    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val reservationApi: ReservationApiRest =
        ReservationRetrofitInstance.retrofitInstance.create(
            ReservationApiRest::class.java
        )

    init {
        viewModelScope.launch {
            try {
                val response = reservationApi.getUserReservationList(userEmail)
                if (response.isSuccessful) {
                    Log.d("RewardsViewModel", "USER RESERVATIONS ACQUIRED SUCCESS")
                    _reservationList.value = response.body()
                } else if (response.code() == 500) {
                    Log.e(
                        "RewardsViewModel",
                        "BACKEND EXCEPTION: ${response.errorBody()?.string()}"
                    )
                    _backendException.value = "Error amb el servidor!"
                }
            } catch (e: Exception) {
                Log.e("RewardsViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    fun loadRewardDetail(id: Long) {
        viewModelScope.launch {
            try {
                val response = reservationApi.getReservationDetail(id)
                if (response.isSuccessful) {
                    Log.d("RewardsViewModel", "RESERVATION ACQUIRED SUCCESS")
                    _reservationDetail.value = response.body()
                } else if (response.code() == 404) {
                    Log.e("RewardsViewModel", "RESERVATION_NOT_FOUND!")
                    _reservationNotFoundError.value = "No s'ha trobat la Recompensa!"
                } else if (response.code() == 500) {
                    Log.e(
                        "RewardsViewModel",
                        "BACKEND EXCEPTION: ${response.errorBody()?.string()}"
                    )
                    _backendException.value = "Error amb el servidor!"
                }
            } catch (e: Exception) {
                Log.e("RewardsViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }
}


