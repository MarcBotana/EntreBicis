package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.LoginRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.LoginApiRest
import cat.copernic.mbotana.entrebicis_frontend.core.enums.Role
import cat.copernic.mbotana.entrebicis_frontend.user.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    //Variable Preparation
    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _role = MutableStateFlow<Role>(Role.BIKER)
    val role: StateFlow<Role> = _role

    //Error Messages
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
    private val _isUserLogged = MutableStateFlow<Boolean>(false)
    val isUserLogged: StateFlow<Boolean> = _isUserLogged

    //Update Functions
    fun updateEmail(value: String) {
        _email.value = value
        _emptyEmailError.value = null
        _emailError.value = null
    }

    fun updatePassword(value: String) {
        _password.value = value
        _emptyPasswordError.value = null
        _passwordError.value = null
    }

    fun resetUserLogged() {
        _isUserLogged.value = false
    }

    //Api Instance
    private val loginApi: LoginApiRest = LoginRetrofitInstance.retrofitInstance.create(
        LoginApiRest::class.java
    )

    suspend fun loginUser(): User? {
        return withContext(Dispatchers.IO) {
            var savedUser: User? = null
            try {
                val isFormValid = checkFormData()

                if (isFormValid) {

                    val response = loginApi.validateUser(_email.value, _password.value)
                    if (response.isSuccessful) {
                        savedUser = response.body()
                        if (savedUser != null) {
                            if (savedUser.isPasswordChanged ) {
                                Log.d("LoginViewModel", "Usuari loginat amb éxit! $savedUser")
                                _isUserLogged.value = true
                            }else {
                                Log.d("LoginViewModel", "Has de modificar la contrasenya inicial! $savedUser")
                                _isUserLogged.value = false
                                _passwordError.value = "Has de modificar la contrasenya inicial!"
                                savedUser = null
                            }
                        }
                    } else if (response.code() == 404) {
                        Log.e(
                            "LoginViewModel",
                            "No s'ha trobat el correu: ${response.errorBody()?.string()} "
                        )
                        _isUserLogged.value = false
                        _emailNotFoundError.value = "Correu no registrat!"
                    } else if (response.code() == 401) {
                        Log.e(
                            "LoginViewModel",
                            "Correu o contrasenya incorrectes!"
                        )
                        _isUserLogged.value = false
                        _emailNotFoundError.value = "Correu o contrasenya incorrectes!"
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exepció al iniciar sessió: ${e.message}")
                _isUserLogged.value = false
            }
            savedUser
        }

    }

    private fun checkFormData(): Boolean {
        var valid = true

        if (_email.value.isEmpty()) {
            _emptyEmailError.value = "El camp no pot estar buit!"
            valid = false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            _emailError.value = "El correu electrònic no és vàlid!"
            valid = false
        }

        if (_password.value.length < 8) {
            _passwordError.value = "La contrasenya ha de tenir almenys 8 caràcters!"
            valid = false
        }

        if (_password.value.isEmpty()) {
            _emptyPasswordError.value = "El camp no pot estar buit!"
            valid = false
        }

        return valid
    }
}