package cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories.UserRetrofitInstance
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote.UserApiRest
import cat.copernic.mbotana.entrebicis_frontend.core.enums.Role
import cat.copernic.mbotana.entrebicis_frontend.core.session.model.SessionUser
import cat.copernic.mbotana.entrebicis_frontend.core.session.repository.SessionRepository
import cat.copernic.mbotana.entrebicis_frontend.user.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SessionViewModel(private val sessionRepository: SessionRepository) : ViewModel() {

    private val _userSession = MutableStateFlow(SessionUser("", Role.BIKER,0.0, false))
    val userSession: StateFlow<SessionUser> get() = _userSession

    private val _userData = MutableStateFlow<User?>(null)
    private val userData: StateFlow<User?> get() = _userData

    private val userApi: UserApiRest = UserRetrofitInstance.retrofitInstance.create(
        UserApiRest::class.java
    )

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            sessionRepository.getSession().collect { session ->
                _userSession.value = session
                Log.i("SessionINFO", _userSession.value.email)
                Log.i("SessionINFO", _userSession.value.role.toString())
                Log.i("SessionINFO", _userSession.value.totalPoints.toString())
                Log.i("SessionINFO", _userSession.value.isConnected.toString())
                if (userSession.value.email.isNotBlank()) {
                    val response = userApi.getUserByEmail(userSession.value.email)
                    updateUserData(response.body())
                    userData.value?.let { Log.i("SessionINFO", it.email) }
                } else {
                    Log.w("SessionINFO", "No email in userSession!")
                }
            }
        }
    }

    private fun updateUserData(user: User?) {
        _userData.value = user
    }

    fun logout() {
        _userSession.value = SessionUser("", Role.BIKER, 0.0,false)
        _userData.value = null
        viewModelScope.launch {
            sessionRepository.saveSession(SessionUser("", Role.BIKER, 0.0,false))
        }
    }

    fun updateSession(sessionUser: SessionUser) {
        viewModelScope.launch {
            sessionRepository.saveSession(sessionUser)
            _userSession.value = sessionUser
        }
    }
}
