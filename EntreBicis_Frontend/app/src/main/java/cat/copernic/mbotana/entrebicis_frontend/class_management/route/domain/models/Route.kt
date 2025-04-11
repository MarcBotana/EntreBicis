package cat.copernic.mbotana.entrebicis_frontend.route.domain.models

import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteState
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteValidate
import cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoints.domain.models.GpsPoints
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
    var gpsPoints: List<GpsPoints>,
    var user: User
)
