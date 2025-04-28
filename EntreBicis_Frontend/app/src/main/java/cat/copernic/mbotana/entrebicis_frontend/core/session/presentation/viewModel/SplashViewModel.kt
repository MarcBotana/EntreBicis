package cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.LoginRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.UserRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.LoginApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.UserApiRest
import cat.copernic.mbotana.entrebicis_frontend.user.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded

    //Error Messages
    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val userApi: UserApiRest = UserRetrofitInstance.retrofitInstance.create(
        UserApiRest::class.java
    )

    suspend fun loadUserData(email: String) {
        return withContext(Dispatchers.IO) {
            viewModelScope.launch {
                try {
                    val response = userApi.getUserByEmail(email)
                    if (response.isSuccessful) {

                        Log.d("SplashViewModel", "USER SESSION SUCCESS")
                        _user.value = response.body()
                        _isDataLoaded.value = true

                    } else if (response.code() == 404) {
                        Log.e("SplashViewModel", "EMAIL_NOT_FOUND!")
                        _backendException.value = "Error amb el servidor!"
                    } else if (response.code() == 500) {
                        Log.e("SplashViewModel", "BACKEND EXCEPTION")
                        _backendException.value = "Error amb el servidor!"
                    }

                } catch (e: Exception) {
                    Log.e("LoginViewModel", "FRONTEND EXCEPTION: ${e.message}")
                    _frontendException.value = "Error amb el client!"
                }
            }
        }
    }
}