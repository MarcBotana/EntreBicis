package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.source.remote

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApiRest {

    @POST("create/{email}/{rewardId}")
    suspend fun createReservation(@Path("email") email: String, @Path("rewardId") rewardId: Long): Response<Void>
}