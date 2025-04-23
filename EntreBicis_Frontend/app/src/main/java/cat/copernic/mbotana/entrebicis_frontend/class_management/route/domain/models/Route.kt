package cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models

import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteValidate
import cat.copernic.mbotana.entrebicis_frontend.class_management.gpsPoint.domain.models.GpsPoint
import cat.copernic.mbotana.entrebicis_frontend.user.domain.models.User

data class Route(
    var id: Long? = null,
    var routeValidate: RouteValidate,
    var totalRoutePoints: Double?,
    var totalRouteDistance: Double,
    var totalRouteTime: String?,
    var maxRouteVelocity: Double,
    var avgRouteVelocity: Double,
    var gpsPoints: List<GpsPoint>?,
    var user: User?
)
