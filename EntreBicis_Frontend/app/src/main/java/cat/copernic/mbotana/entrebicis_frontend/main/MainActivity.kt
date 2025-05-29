package cat.copernic.mbotana.entrebicis_frontend.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import cat.copernic.mbotana.entrebicis_frontend.core.session.repository.SessionRepository
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.ViewModelFactory
import cat.copernic.mbotana.entrebicis_frontend.navigation.AppNavigation

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class MainActivity : ComponentActivity() {

    private lateinit var sessionRepository: SessionRepository
    private lateinit var sessionViewModel: SessionViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        setContent {
            sessionRepository = SessionRepository(dataStore = applicationContext.dataStore)
            sessionViewModel = ViewModelProvider(
                this,
                ViewModelFactory(sessionRepository)
            ).get(SessionViewModel::class.java)
            AppNavigation(sessionViewModel = sessionViewModel)
        }
    }
}

