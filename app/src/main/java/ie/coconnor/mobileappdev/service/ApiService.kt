package ie.coconnor.mobileappdev.service

import ie.coconnor.mobileappdev.models.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//    @GET("data/{location_id}")
//    fun getPostById(@Path("location_id") postId: Int): Call<TripadvisorJson>

    @GET("location/search")
    suspend fun getLocations(@Query("key") apiKey: String,
                     @Query("searchQuery") searchQuery: String,
                     @Query("category") category: String): LocationResponse
//    @GET("location/{location_id}/details")
//    suspend fun getLocationById(@Path("location_id") id: String,
//                        @Query("key") apiKey: String): LocationDetails
}