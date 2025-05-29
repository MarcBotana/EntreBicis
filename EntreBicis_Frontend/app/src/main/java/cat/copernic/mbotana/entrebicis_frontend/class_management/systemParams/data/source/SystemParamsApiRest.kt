package cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.data.source

import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.domain.models.SystemParams
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SystemParamsApiRest {

    @GET("list/all")
    suspend fun getAllSystemParams(): Response<List<SystemParams>>

    @GET("detail/{id}")
    suspend fun getSystemParamsById(@Path("id") id: Long): Response<SystemParams>
}