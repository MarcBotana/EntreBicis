package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.UserRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.UserApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileViewModel: ViewModel() {



    private val _userDetail = MutableStateFlow<User?>(null)
    val userDetail: StateFlow<User?> = _userDetail

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _surname = MutableStateFlow<String>("")
    val surname: StateFlow<String> = _surname

    private val _observation = MutableStateFlow<String>("")
    val observation: StateFlow<String> = _observation

    private val _town = MutableStateFlow<String>("")
    val town: StateFlow<String> = _town

    private val _mobile = MutableStateFlow<Int>(0)
    val mobile: StateFlow<Int> = _mobile

    private val _updateUserSuccess = MutableStateFlow(false)
    val updateUserSuccess: StateFlow<Boolean> = _updateUserSuccess


    private val _emptyNameError = MutableStateFlow<String?>(null)
    val emptyNameError: StateFlow<String?> = _emptyNameError

    private val _emptySurnameError = MutableStateFlow<String?>(null)
    val emptySurnameError: StateFlow<String?> = _emptySurnameError

    private val _emptyObservationError = MutableStateFlow<String?>(null)
    val emptyObservationError: StateFlow<String?> = _emptyObservationError

    private val _emptyTownError = MutableStateFlow<String?>(null)
    val emptyTownError: StateFlow<String?> = _emptyTownError

    private val _emptyMobileError = MutableStateFlow<String?>(null)
    val emptyMobileError: StateFlow<String?> = _emptyMobileError

    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _userNotFoundError = MutableStateFlow<String?>(null)
    val userNotFoundError: StateFlow<String?>  = _userNotFoundError

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val userApi: UserApiRest = UserRetrofitInstance.retrofitInstance.create(
        UserApiRest::class.java
    )

    fun updateName(value: String) {
        _name.value = value
    }
    fun updateSurname(value: String) {
        _surname.value = value
    }

    fun updateObservation(value: String) {
        _observation.value = value
    }

    fun updateTown(value: String) {
        _town.value = value
    }
    fun updateMobile(value: Int) {
        _mobile.value = value
    }



    fun loadUserProfile(email: String) {
        viewModelScope.launch {
            try {
                val response = userApi.getUserByEmail(email)
                if (response.isSuccessful) {
                    Log.d("UserProfileViewModel", "USER ACQUIRED SUCCESS")
                    _userDetail.value = response.body()

                    _userDetail.value?.let { updateName(it.name) }
                    _userDetail.value?.let { updateSurname(it.surname) }
                    _userDetail.value?.let { updateObservation(it.observation) }
                    _userDetail.value?.let { updateTown(it.town) }
                    _userDetail.value?.let { updateMobile(it.mobile) }

                } else if (response.code() == 404) {
                    Log.e("UserProfileViewModel", "USER_NOT_FOUND!")
                    _userNotFoundError.value = "No s'ha trobat l'Usuari!"
                } else if (response.code() == 500) {
                    Log.e("UserProfileViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
                    _backendException.value = "Error amb el servidor!"
                }
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    suspend fun updateUser() {
        return withContext(Dispatchers.IO) {
            viewModelScope.launch {
                try {
                    val isFormValid = checkDataForm()
                    if (isFormValid) {
                        val userDB = _userDetail.value?.let {
                            userApi.getUserByEmail(it.email).body()
                        }
                        if (userDB != null) {
                            userDB.name = _name.value
                            userDB.surname = _surname.value
                            userDB.observation = _observation.value
                            userDB.town = _town.value
                            userDB.mobile = _mobile.value

                            val response = userApi.updateUser(userDB)
                            if (response.isSuccessful) {
                                Log.d("UserProfileViewModel", "USER UPDATE SUCCESS!")
                                _updateUserSuccess.value = true
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("UserProfileViewModel", "FRONTEND EXCEPTION: ${e.message}")
                    _frontendException.value = "Error amb el client!"
                }
            }
        }
    }

    private fun checkDataForm(): Boolean {
        var valid = true

        if (_name.value.isEmpty()) {
            _emptyNameError.value = "El camp no pot estar buit!"
            valid = false
        }
        if (_surname.value.isEmpty()) {
            _emptySurnameError.value = "El camp no pot estar buit!"
            valid = false
        }
        if (_observation.value.isEmpty()) {
            _emptyObservationError.value = "El camp no pot estar buit!"
            valid = false
        }
        if (_town.value.isEmpty()) {
            _emptyTownError.value = "El camp no pot estar buit!"
            valid = false
        }
        if (_mobile.value > 999999999 || _mobile.value < 100000000 || _mobile.value == 0) {
            _emptyMobileError.value = "Número invàlid!"
            valid = false
        }

        return valid
    }


}