package cat.copernic.mbotana.entrebicis_frontend.reward_management.domain.models

import cat.copernic.mbotana.entrebicis_frontend.core.enums.RewardState
import cat.copernic.mbotana.entrebicis_frontend.exchangePoint_management.domain.models.ExchangePoint
import cat.copernic.mbotana.entrebicis_frontend.reservation_management.domain.models.Reservation

data class Reward(
    var id: Long? = null,
    var name: String,
    var description: String,
    var observation: String,
    var image: String,
    var valuePoints: Double,
    var rewardState: RewardState,
    var exchangePoint: ExchangePoint,
    var reservation: Reservation,
)
