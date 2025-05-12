package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.LoginRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.LoginApiRest
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    //Variable Preparation
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user


    //Error Messages
    private val _backendException = MutableStateFlow<String?>(null)
    val backendException: StateFlow<String?> = _backendException

    private val _frontendException = MutableStateFlow<String?>(null)
    val frontendException: StateFlow<String?> = _frontendException

    private val _emptyEmailError = MutableStateFlow<String?>(null)
    val emptyEmailError: StateFlow<String?> = _emptyEmailError

    private val _emptyPasswordError = MutableStateFlow<String?>(null)
    val emptyPasswordError: StateFlow<String?> = _emptyPasswordError

    private val _emailNotFoundError = MutableStateFlow<String?>(null)
    val emailNotFoundError: StateFlow<String?> = _emailNotFoundError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _unauthorizedError = MutableStateFlow<String?>(null)
    val unauthorizedError: StateFlow<String?> = _unauthorizedError

    //Others
    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> = _isUserLogged

    //Update Functions
    fun updateEmail(value: String) {
        _email.value = value
        _emptyEmailError.value = null
        _emailError.value = null
        _emailNotFoundError.value = null
        _unauthorizedError.value = null
    }

    fun updatePassword(value: String) {
        _password.value = value
        _emptyPasswordError.value = null
        _passwordError.value = null
        _unauthorizedError.value = null
    }

    fun resetUserLogged() {
        _isUserLogged.value = false
    }

    //Api Instance
    private val loginApi: LoginApiRest = LoginRetrofitInstance.retrofitInstance.create(
        LoginApiRest::class.java
    )

    suspend fun loginUser() {
        return withContext(Dispatchers.IO) {
            var savedUser: User? = null
            try {
                val isFormValid = checkDataForm()

                if (isFormValid) {

                    val response = loginApi.validateUser(_email.value, _password.value)
                    if (response.isSuccessful) {
                        savedUser = response.body()
                        if (savedUser != null) {
                            if (savedUser.isPasswordChanged) {
                                Log.d("LoginViewModel", "USER LOGGED SUCCESS: $savedUser")
                                _isUserLogged.value = true
                                _user.value = savedUser
                            }
                        }
                    } else if (response.code() == 409) {
                        Log.e("LoginViewModel", "NO PASSWORD CHANGED: $savedUser")
                        _passwordError.value = "Has de fer el canvi de contrasenya inicial!"
                    } else if (response.code() == 401 || response.code() == 404) {
                        Log.e("LoginViewModel", "EMAIL OR PASSWORD ERROR! (UNAUTHORIZED)")
                        _emailNotFoundError.value = "Correu o contrasenya incorrectes!"
                    } else if (response.code() == 500) {
                        Log.e("LoginViewModel", "BACKEND EXCEPTION: ${response.errorBody()?.string()}")
                        _backendException.value = "Error amb el servidor!"
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "FRONTEND EXCEPTION: ${e.message}")
                _frontendException.value = "Error amb el client!"
            }
        }
    }

    private fun checkDataForm(): Boolean {
        var valid = true

        if (_email.value.isEmpty()) {
            _emptyEmailError.value = "El camp no pot estar buit!"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            _emailError.value = "El correu electrònic no és vàlid!"
            valid = false
        }

        if (_password.value.isEmpty()) {
            _emptyPasswordError.value = "El camp no pot estar buit!"
            valid = false
        } else if (_password.value.length < 4) {
            _passwordError.value = "La contrasenya ha de tenir almenys 4 caràcters!"
            valid = false
        }

        return valid
    }
}