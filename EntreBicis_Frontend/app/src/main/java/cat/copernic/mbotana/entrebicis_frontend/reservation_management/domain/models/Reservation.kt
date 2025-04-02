package cat.copernic.mbotana.entrebicis_frontend.reservation_management.domain.models

import cat.copernic.mbotana.entrebicis_frontend.reward_management.domain.models.Reward
import cat.copernic.mbotana.entrebicis_frontend.user_management.domain.models.User
import java.time.LocalTime

data class Reservation(
    var id: Long? = null,
    var reservationTime: LocalTime,
    var returnTime: LocalTime,
    var user: User,
    var reward: Reward
)
