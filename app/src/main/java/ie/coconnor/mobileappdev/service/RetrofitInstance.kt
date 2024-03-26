package ie.coconnor.mobileappdev.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.content.tripadvisor.com/api/v1/" //location/search?key={KEY}}&searchQuery=waterford&language=en"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val tourService: TourService by lazy {
        retrofit.create(TourService::class.java)
    }
}