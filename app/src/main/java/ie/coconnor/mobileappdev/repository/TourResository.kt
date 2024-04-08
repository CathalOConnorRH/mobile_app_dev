package ie.coconnor.mobileappdev.repository

import ie.coconnor.mobileappdev.models.LocationResponse
import ie.coconnor.mobileappdev.service.RetrofitInstance

class TourRepository {

    private val tourService = RetrofitInstance.apiService

    suspend fun getLocations(apiKey: String, searchQuery: String, category: String): LocationResponse {
        return tourService.getLocations(apiKey,searchQuery,category)
    }
//    suspend fun getLocations(apiKey: String, searchQuery: String, category: String): Call<LocationResponse> {
//        return tourService.getLocations(apiKey,searchQuery,category)
//    }
}