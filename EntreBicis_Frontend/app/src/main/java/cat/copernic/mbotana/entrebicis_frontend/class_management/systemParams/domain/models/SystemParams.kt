package cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.domain.models

data class SystemParams(
    var id: Long? = null,
    var paramsTitle: String,
    var paramsDesc: String,
    var maxVelocity: Int,
    var pointsConversion: Double,
    var stopMaxTime: Int,
    var collectionMaxTime: Int,
)
