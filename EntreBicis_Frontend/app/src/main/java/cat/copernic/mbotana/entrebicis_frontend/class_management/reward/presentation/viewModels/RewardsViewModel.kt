package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.repositories.ReservationRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.data.repositories.RewardRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.source.remote.ReservationApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.data.source.remote.RewardApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RewardsViewModel: ViewModel() {

    //Variable Preparation
    private val _rewardsList = MutableStateFlow<List<Reward>?>(emptyList())
    val rewardsList: StateFlow<List<Reward>?> = _rewardsList

    private val _rewardDetail = MutableStateFlow<Reward?>(null)
    val rewardDetail: StateFlow<Reward?> = _rewardDetail

    private val _reservationSuccess = MutableStateFlow<Boolean>(false)
    val reservationSuccess: StateFlow<Boolean> = _reservationSuccess

    //Error Messages
    private val _rewardNotFoundError = MutableStateFlow<String?>(null)
    val rewardNotFoundError: StateFlow<String?>  = _rewardNotFoundError

    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val rewardApi: RewardApiRest = RewardRetrofitInstance.retrofitInstance.create(
        RewardApiRest::class.java
    )

    private val reservationApi: ReservationApiRest = ReservationRetrofitInstance.retrofitInstance.create(
        ReservationApiRest::class.java
    )


    fun loadData() {
        viewModelScope.launch {
            try {
                val response = rewardApi.getAvailableRewardList()
                if (response.isSuccessful) {
                    Log.d("RewardsViewModel", "REWARDS ACQUIRED SUCCESS")
                    _rewardsList.value = response.body()
                } else if (response.code() == 500) {
                    Log.e("RewardsViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
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
                val response = rewardApi.getRewardDetail(id)
                if (response.isSuccessful) {
                    Log.d("RewardsViewModel", "REWARDS ACQUIRED SUCCESS")
                    _rewardDetail.value = response.body()
                } else if (response.code() == 404) {
                    Log.e("RewardsViewModel", "REWARD_NOT_FOUND!")
                    _rewardNotFoundError.value = "No s'ha trobat la Recompensa!"
                } else if (response.code() == 500) {
                    Log.e("RewardsViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
                    _backendException.value = "Error amb el servidor!"
                }
            } catch (e: Exception) {
                Log.e("RewardsViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    fun makeReservation(email: String, rewardId: Long) {
        viewModelScope.launch {
            try {
                val response = reservationApi.createReservation(email, rewardId)
                if (response.isSuccessful) {
                    Log.e("RewardsViewModel", "RESERVATION_SUCCESS!")
                    _reservationSuccess.value = true
                } else if (response.code() == 402) {
                    Log.e("RewardsViewModel", "USER LOW POINT VALUE! ")
                    _backendException.value = "No tens suficients punts!"
                } else if (response.code() == 409) {
                    Log.e("RewardsViewModel", "USER HAS RESERVATION ACTIVE! ")
                    _backendException.value = "Ja tens una reserva activa!"
                } else if (response.code() == 500) {
                    Log.e("RewardsViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
                    _backendException.value = "Error amb el servidor!"
                }

            } catch (e: Exception) {
                Log.e("RewardsViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }
}


