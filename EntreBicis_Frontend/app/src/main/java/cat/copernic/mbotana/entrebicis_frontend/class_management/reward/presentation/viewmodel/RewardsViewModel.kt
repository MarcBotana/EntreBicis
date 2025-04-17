package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.data.repositories.RewardRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.data.source.remote.RewardApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RewardsViewModel: ViewModel() {

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search

    //Variable Preparation
    private val _rewardsList = MutableStateFlow<List<Reward>?>(emptyList())
    val rewardsList: StateFlow<List<Reward>?> = _rewardsList

    //Error Messages
    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    fun updateSearch(value: String) {
        _search.value = value
    }

    private val rewardApi: RewardApiRest = RewardRetrofitInstance.retrofitInstance.create(
        RewardApiRest::class.java
    )


    init {
        viewModelScope.launch {
            try {
                val response = rewardApi.getAvailableRewardList()
                if (response.isSuccessful) {
                    Log.d("RewardsViewModel", "REWARDS ACQUIRED SUCCESS: ")
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
}


