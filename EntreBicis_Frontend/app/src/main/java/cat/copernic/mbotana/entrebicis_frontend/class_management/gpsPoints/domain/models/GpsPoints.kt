package cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoints.domain.models

import cat.copernic.mbotana.entrebicis_frontend.route.domain.models.Route
import java.time.LocalTime

data class GpsPoints(
    var id: Long? = null,
    var latitude: Double,
    var longitude: Double,
    var time: LocalTime,
    var route: Route
)
