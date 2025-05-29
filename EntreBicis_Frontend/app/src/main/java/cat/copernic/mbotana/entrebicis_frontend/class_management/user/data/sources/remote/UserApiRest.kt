package cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote

import cat.copernic.mbotana.entrebicis_frontend.class_management.user.domain.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiRest {

    @GET("getUserEmail/{email}")
    suspend fun getUserByEmail(@Path("email") email : String) : Response<User>

    @PUT("update")
    suspend fun updateUser(@Body user: User) : Response<Void>

    @PUT("updatePassword")
    suspend fun updateUserPassword(@Body user: User) : Response<Void>
}