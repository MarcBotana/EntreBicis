package cat.copernic.mbotana.entrebicis_frontend.class_management.user.data.repositories

import cat.copernic.mbotana.entrebicis_frontend.core.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRetrofitInstance {

    private const val URL = Constants.BASE_URL + "/user"

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}