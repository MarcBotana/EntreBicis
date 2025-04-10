package cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoint.domain.models

import cat.copernic.mbotana.entrebicis_frontend.route.domain.models.Route
import java.time.LocalTime

data class GpsPoint(
    var id: Long? = null,
    var latitude: Double,
    var longitude: Double,
    var time: LocalTime,
    var route: Route
)
