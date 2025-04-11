package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RewardsViewModel: ViewModel() {

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search



    fun updateSearch(value: String) {
        _search.value = value
    }
}