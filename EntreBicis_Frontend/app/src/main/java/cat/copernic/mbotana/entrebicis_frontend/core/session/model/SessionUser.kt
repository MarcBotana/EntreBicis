package cat.copernic.mbotana.entrebicis_frontend.core.session.model

import cat.copernic.mbotana.entrebicis_frontend.core.enums.Role

data class SessionUser(
    val email: String,
    val image: String,
    val role: Role,
    val totalPoints: Double,
    val isConnected: Boolean
)


