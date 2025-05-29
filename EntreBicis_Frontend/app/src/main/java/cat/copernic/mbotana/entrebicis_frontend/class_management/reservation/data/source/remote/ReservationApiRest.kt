package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.source.remote

import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.domain.models.Reservation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReservationApiRest {

    @POST("create/{email}/{rewardId}")
    suspend fun createReservation(@Path("email") email: String, @Path("rewardId") rewardId: Long): Response<Void>

    @GET("list/{email}")
    suspend fun getUserReservationList(@Path("email") email: String): Response<List<Reservation>>

    @GET("detail/{id}")
    suspend fun getReservationDetail(@Path("id") id: Long): Response<Reservation>

    @PUT("collect/{id}/{email}")
    suspend fun collectReservation(@Path("id") id: Long, @Path("email") email: String): Response<Void>

}