package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewmodels

import androidx.lifecycle.ViewModel
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.LoginRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.LoginApiRest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    //Variable Preparation
    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

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
    private val _isUserLoged = MutableStateFlow<Boolean>(false)
    val isUserLoged: StateFlow<Boolean> = _isUserLoged

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
        _isUserLoged.value = false
    }

    //Api Instance
    private val loginApi: LoginApiRest = LoginRetrofitInstance.retrofitInstance.create(
        LoginApiRest::class.java
    )




}