package cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.sources.remote

import cat.copernic.mbotana.entrebicis_frontend.class_management.user.domain.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginApiRest {

    @GET("validate/{email}/{password}")
    suspend fun validateUser(@Path("email") email : String, @Path("password") password : String) : Response<User>

    @POST("sendEmail/{email}")
    suspend fun sendEmail(@Path("email") email: String) : Response<Void>

    @GET("validateToken/{token}/{email}")
    suspend fun validateToken(@Path("token") token : String, @Path("email") email: String) : Response<Boolean>

}