package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.data.repositories

import cat.copernic.mbotana.entrebicis_frontend.core.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ReservationRetrofitInstance {

    private const val URL = Constants.BASE_URL + "/reservation/"

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}