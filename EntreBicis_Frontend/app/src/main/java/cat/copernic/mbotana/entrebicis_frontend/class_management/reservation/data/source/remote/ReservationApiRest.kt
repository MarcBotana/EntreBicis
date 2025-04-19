package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.source.remote

import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReservationApiRest {

    @GET("list/all")
    suspend fun getAllRewardList(): Response<List<Reward>>

    @GET("list/available")
    suspend fun getAvailableRewardList(): Response<List<Reward>>

    @GET("detail/{id}")
    suspend fun getRewardDetail(@Path("id") id: Long): Response<Reward>
}