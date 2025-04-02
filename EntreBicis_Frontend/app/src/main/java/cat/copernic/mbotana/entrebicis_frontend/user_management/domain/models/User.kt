package cat.copernic.mbotana.entrebicis_frontend.user_management.domain.models

import cat.copernic.mbotana.entrebicis_frontend.core.enums.Role
import cat.copernic.mbotana.entrebicis_frontend.core.enums.UserState
import cat.copernic.mbotana.entrebicis_frontend.reservation_management.domain.models.Reservation
import cat.copernic.mbotana.entrebicis_frontend.route_management.domain.models.Route

data class User(
    var email: String,
    var role: Role,
    var name: String,
    var surname: String,
    var password: String,
    var town: String,
    var mobile: Int,
    var image: String,
    var isRouteStarted: Boolean,
    var isPasswordChanged: Boolean,
    var userState: UserState,
    var totalPoints: Double,
    var routes: List<Route>,
    var reservations: List<Reservation>
)
