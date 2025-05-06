package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.domain.models

import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import cat.copernic.mbotana.entrebicis_frontend.core.enums.ReservationState
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.domain.models.User


data class Reservation(
    var id: Long? = null,
    var reservationCode: String,
    var reservationState: ReservationState,
    var returnTime: String,
    var reservationDate: String,
    var assignationDate: String,
    var returnDate: String,
    var user: User?,
    var reward: Reward
)
