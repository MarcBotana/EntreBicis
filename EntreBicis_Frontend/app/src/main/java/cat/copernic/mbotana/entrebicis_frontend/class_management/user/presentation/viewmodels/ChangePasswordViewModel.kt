package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.LoginRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.UserRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.LoginApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.UserApiRest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {

    //Variable Preparation
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _tokenCode = MutableStateFlow("")
    val tokenCode: StateFlow<String> = _tokenCode

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _repNewPassword = MutableStateFlow("")
    val repNewPassword: StateFlow<String> = _repNewPassword

    //Ok Messages
    private val _sendEmailSuccess = MutableStateFlow<Boolean>(false)
    val sendEmailSuccess: StateFlow<Boolean> = _sendEmailSuccess

    private val _tokenCodeSuccess = MutableStateFlow<Boolean>(false)
    val tokenCodeSuccess: StateFlow<Boolean> = _tokenCodeSuccess

    //Error Messages
    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val _emptyEmailError = MutableStateFlow<String?>(null)
    val emptyEmailError: StateFlow<String?> = _emptyEmailError

    private val _emptyTokenCodeError = MutableStateFlow<String?>(null)
    val emptyTokenCodeError: StateFlow<String?> = _emptyTokenCodeError

    private val _emptyNewPasswordError = MutableStateFlow<String?>(null)
    val emptyNewPasswordError: StateFlow<String?> = _emptyNewPasswordError

    private val _emptyRepNewPasswordError = MutableStateFlow<String?>(null)
    val emptyRepNewPasswordError: StateFlow<String?> = _emptyRepNewPasswordError

    private val _emailNotFoundError = MutableStateFlow<String?>(null)
    val emailNotFoundError: StateFlow<String?> = _emailNotFoundError

    private val _tokenCodeNotFoundError = MutableStateFlow<String?>(null)
    val tokenCodeNotFoundError: StateFlow<String?> = _tokenCodeNotFoundError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _tokenCodeError = MutableStateFlow<String?>(null)
    val tokenCodeError: StateFlow<String?> = _tokenCodeError

    private val _newPasswordError = MutableStateFlow<String?>(null)
    val newPasswordError: StateFlow<String?> = _newPasswordError

    private val _repNewPasswordError = MutableStateFlow<String?>(null)
    val repNewPasswordError: StateFlow<String?> = _repNewPasswordError

    private val _passwordNotMatchError = MutableStateFlow<String?>(null)
    val passwordNotMatchError: StateFlow<String?> = _passwordNotMatchError

    //Others
    private val _isPasswordChanged = MutableStateFlow(false)
    val isPasswordChanged: StateFlow<Boolean> = _isPasswordChanged

    //Update Functions
    fun updateEmail(value: String) {
        _email.value = value
        _emptyEmailError.value = null
        _emailError.value = null
    }

    fun updateTokenCode(value: String) {
        _tokenCode.value = value
        _emptyTokenCodeError.value = null
        _tokenCodeError.value = null
    }

    fun updateNewPassword(value: String) {
        _newPassword.value = value
        _emptyNewPasswordError.value = null
        _newPasswordError.value = null
        _passwordNotMatchError.value = null
    }

    fun updateRepNewPassword(value: String) {
        _repNewPassword.value = value
        _emptyRepNewPasswordError.value = null
        _repNewPasswordError.value = null
        _passwordNotMatchError.value = null
    }

    fun resetPasswordChanged() {
        _isPasswordChanged.value = false
    }

    //Api Instance
    private val loginApi: LoginApiRest = LoginRetrofitInstance.retrofitInstance.create(
        LoginApiRest::class.java
    )

    private val userApi: UserApiRest = UserRetrofitInstance.retrofitInstance.create(
        UserApiRest::class.java
    )

    fun sendEmail() {
        viewModelScope.launch {
            try {
                val isEmailValid = checkEmailForm()
                if (isEmailValid) {
                    val response = loginApi.sendEmail(_email.value)
                    if (response.isSuccessful) {
                        Log.d("ChangePasswordViewModel", "SEND EMAIL SUCCESS!")
                        _sendEmailSuccess.value = true
                    } else if (response.code() == 404) {
                        Log.e("ChangePasswordViewModel", "EMAIL_NOT_FOUND: ${response.errorBody()?.string()}")
                        _emailNotFoundError.value = "Correu no registrat!"
                    } else if (response.code() == 500) {
                        Log.e("ChangePasswordViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
                        _backendException.value = "Error amb el servidor!"
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    fun validateTokenCode() {
        viewModelScope.launch {
            try {
                val isTokenValid = checkTokenCodeForm()
                if (isTokenValid) {
                    val response = loginApi.validateToken(_tokenCode.value, _email.value)
                    if (response.isSuccessful) {
                        Log.d("ChangePasswordViewModel", "TOKEN CODE SUCCESS!")
                        _tokenCodeSuccess.value = true
                    } else if (response.code() == 404) {
                        Log.d("ChangePasswordViewModel", "TOKEN CODE NOT FOUND!")
                        _tokenCodeNotFoundError.value = "El codi no és vàlid!"
                    } else if (response.code() == 401) {
                        Log.d("ChangePasswordViewModel", "TOKEN CODE EXPIRED! (UNAUTHORIZED)")
                        _tokenCodeError.value = "El codi ha expirat!"
                    } else if (response.code() == 400) {
                        Log.d("ChangePasswordViewModel", "TOKEN CODE NOT FOUND!")
                        _tokenCodeNotFoundError.value = "El codi no és vàlid!"
                    } else if (response.code() == 500) {
                        Log.e("ChangePasswordViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
                        _backendException.value = "Error amb el servidor!"
                    }
                }
            } catch (e: Exception) {
                Log.e("ChangePasswordViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    fun updatePasswordUser() {
        viewModelScope.launch {
            try {
                val isNewPasswordValid = checkPasswordForm()
                if (isNewPasswordValid) {
                    val userDB = userApi.getUserByEmail(_email.value).body()
                    if (userDB != null) {
                        userDB.password = _newPassword.value
                        val response = userApi.updateUser(userDB)
                        if (response.isSuccessful) {
                            Log.d("ChangePasswordViewModel", "PASSWORD CHANGE SUCCESS!")
                            _isPasswordChanged.value = true
                        }
                    }
                }

            } catch (e: Exception) {

            }
        }
    }

    private fun checkEmailForm(): Boolean {
        var valid = true

        if (_email.value.isEmpty()) {
            _emptyEmailError.value = "El camp no pot estar buit!"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            _emailError.value = "El correu electrònic no és vàlid!"
            valid = false
        }

        return valid
    }

    private fun checkTokenCodeForm(): Boolean {
        var valid = true

        if (_tokenCode.value.isEmpty()) {
            _emptyTokenCodeError.value = "El camp no pot estat buit!"
            valid = false
        }

        return valid
    }

    private fun checkPasswordForm(): Boolean {
        var valid = true

        if (_newPassword.value.isEmpty()) {
            _emptyNewPasswordError.value = "El camp no pot estar buit!"
            valid = false
        } else if (_newPassword.value.length < 4) {
            _newPasswordError.value = "La contrasenya ha de tenir almenys 4 caràcters!"
            valid = false
        }

        if (_repNewPassword.value.isEmpty()) {
            _emptyRepNewPasswordError.value = "El camp no pot estar buit!"
            valid = false
        } else if (_repNewPassword.value.length < 4) {
            _repNewPasswordError.value = "La contrasenya ha de tenir almenys 4 caràcters!"
            valid = false
        }

        if (_newPassword.value != _repNewPassword.value) {
            _newPasswordError
        }

        return valid
    }

}