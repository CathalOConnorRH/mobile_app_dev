package ie.coconnor.mobileappdev.service

import ie.coconnor.mobileappdev.models.locations.LocationDetails
import ie.coconnor.mobileappdev.models.locations.LocationPhotosResponse
import ie.coconnor.mobileappdev.models.locations.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("location/search")
    @Headers("Referer: http://coconnor.ie")
    suspend fun getLocations(@Query("key") apiKey: String,
                     @Query("searchQuery") searchQuery: String,
                     @Query("category") category: String): LocationResponse

    @GET("location/{location_id}/details")
    @Headers("Referer: http://coconnor.ie")
    suspend fun getLocationDetails(@Path("location_id") location_id: String,
                                   @Query("key") apiKey: String): LocationDetails

    @GET("location/{location_id}/photos")
    @Headers("Referer: http://coconnor.ie")
    suspend fun getLocationPhotos(@Path("location_id") location_id: String,
                                   @Query("key") apiKey: String): LocationPhotosResponse
}