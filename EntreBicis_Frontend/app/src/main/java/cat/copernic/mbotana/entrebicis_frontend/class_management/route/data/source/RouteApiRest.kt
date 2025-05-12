package cat.copernic.mbotana.entrebicis_frontend.class_management.route.data.source

import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RouteApiRest {

    @POST("create/{email}")
    suspend fun createRoute(@Path("email") email: String, @Body route: Route): Response<Void>

    @GET("list/{email}")
    suspend fun getUserRoutesList(@Path("email") email: String): Response<List<Route>>

    @GET("detail/{id}")
    suspend fun getRouteDetail(@Path("id") id: Long): Response<Route>
}