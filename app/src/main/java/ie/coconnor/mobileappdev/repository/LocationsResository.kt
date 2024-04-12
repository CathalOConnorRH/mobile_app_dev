package ie.coconnor.mobileappdev.repository

import ie.coconnor.mobileappdev.models.LocationDetails
import ie.coconnor.mobileappdev.models.LocationDetailsResponse
import ie.coconnor.mobileappdev.models.LocationResponse
import ie.coconnor.mobileappdev.service.RetrofitInstance

class LocationsRepository {

    private val locationsService = RetrofitInstance.apiService

    suspend fun getLocations(apiKey: String, searchQuery: String, category: String): LocationResponse {

        return locationsService.getLocations(apiKey,searchQuery,category)
    }

    suspend fun getLocationDetails(location_id: String, apiKey: String, ): LocationDetails {

        println("LOcation ID " + location_id)
        return locationsService.getLocationDetails(location_id, apiKey)
    }
//    suspend fun getLocations(apiKey: String, searchQuery: String, category: String): Call<LocationResponse> {
//        return tourService.getLocations(apiKey,searchQuery,category)
//    }
}