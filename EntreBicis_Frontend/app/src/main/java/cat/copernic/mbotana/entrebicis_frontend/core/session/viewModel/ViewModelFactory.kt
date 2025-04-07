package cat.copernic.mbotana.entrebicis_frontend.core.session.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.mbotana.entrebicis_frontend.core.session.repository.SessionRepository

class ViewModelFactory(private val sessionRepository: SessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SessionViewModel(sessionRepository) as T
    }
}