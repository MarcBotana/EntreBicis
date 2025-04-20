package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.domain.models

import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import cat.copernic.mbotana.entrebicis_frontend.user.domain.models.User


data class Reservation(
    var id: Long? = null,
    var reservationCode: String,
    var reservationTime: String,
    var returnTime: String,
    var user: User,
    var reward: Reward
)
