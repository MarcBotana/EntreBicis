package cat.copernic.mbotana.entrebicis_frontend.core.session.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cat.copernic.mbotana.entrebicis_frontend.core.enums.Role
import cat.copernic.mbotana.entrebicis_frontend.core.session.model.SessionUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val USER_POINTS_KEY = doublePreferencesKey("user_points")
        private val IS_CONNECTED_KEY = booleanPreferencesKey("is_connected")
    }

    suspend fun saveSession(sessionUser: SessionUser) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = sessionUser.email
            preferences[USER_ROLE_KEY] = sessionUser.role.name
            preferences[USER_POINTS_KEY] = sessionUser.totalPoints
            preferences[IS_CONNECTED_KEY] = sessionUser.isConnected
        }
    }

    fun getSession(): Flow<SessionUser> {
        return dataStore.data.map { preferences ->
            val email = preferences[USER_EMAIL_KEY] ?: ""
            val role = Role.valueOf(preferences[USER_ROLE_KEY] ?: Role.BIKER.name)
            val totalPoints = preferences[USER_POINTS_KEY] ?: 0.0
            val isConnected = preferences[IS_CONNECTED_KEY] ?: false
            SessionUser(email, role, totalPoints, isConnected)
        }
    }
}