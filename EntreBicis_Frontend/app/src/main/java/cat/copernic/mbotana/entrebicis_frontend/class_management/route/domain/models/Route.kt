package cat.copernic.mbotana.entrebicis_frontend.route.domain.models

import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteState
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteValidate
import cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoint.domain.models.GpsPoint
import cat.copernic.mbotana.entrebicis_frontend.user.domain.models.User
import java.time.LocalTime

data class Route(
    var id: Long? = null,
    var routeState: RouteState,
    var routeValidate: RouteValidate,
    var totalRoutePoints: Double,
    var totalRouteDistance: Double,
    var totalRouteTime: LocalTime,
    var maxRouteVelocity: Double,
    var avgRouteVelocity: Double,
    var gpsPoints: List<GpsPoint>,
    var user: User
)
