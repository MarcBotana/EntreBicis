package cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoints.domain.models

import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route

data class GpsPoints(
    var id: Long? = null,
    var latitude: Double,
    var longitude: Double,
    var time: String,
    var speed: Float,
    var route: Route?
)
