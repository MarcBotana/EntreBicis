package cat.copernic.mbotana.entrebicis_frontend.class_management.user.domain.models

import cat.copernic.mbotana.entrebicis_frontend.core.enums.Role
import cat.copernic.mbotana.entrebicis_frontend.core.enums.UserState
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.domain.models.Reservation
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route

data class User(
    var email: String,
    var role: Role,
    var name: String,
    var surname: String,
    var password: String,
    var observation: String,
    var town: String,
    var mobile: Int,
    var image: String?,
    var isPasswordChanged: Boolean,
    var isReservationActive: Boolean,
    var userState: UserState,
    var totalPoints: Double,
    var routes: List<Route>,
    var reservations: List<Reservation>
)
