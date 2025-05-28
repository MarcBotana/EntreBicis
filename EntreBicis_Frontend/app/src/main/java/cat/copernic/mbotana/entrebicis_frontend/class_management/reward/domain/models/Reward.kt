package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models

import cat.copernic.mbotana.entrebicis_frontend.core.enums.RewardState
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.domain.models.Reservation

data class Reward(
    var id: Long? = null,
    var name: String,
    var description: String,
    var observation: String,
    var image: String,
    var valuePoints: Double,
    var rewardState: RewardState,
    var rewardDate: String,
    var reservationDate: String,
    var assignationDate: String,
    var returnDate: String,
    var exchangePoint: String,
    var reservation: Reservation?,
)
