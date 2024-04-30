package ie.coconnor.mobileappdev.repository

import ie.coconnor.mobileappdev.models.locations.LocationDetails
import ie.coconnor.mobileappdev.models.locations.LocationPhotosResponse
import ie.coconnor.mobileappdev.models.locations.LocationResponse
import ie.coconnor.mobileappdev.service.RetrofitInstance

class LocationsRepository {

    private val locationsService = RetrofitInstance.apiService

    suspend fun getLocations(apiKey: String, searchQuery: String, category: String): LocationResponse {

        var locations = locationsService.getLocations(apiKey,searchQuery,category)
        if(locations.data.isNotEmpty()){
            locations.data.forEach {
                var photos = it.location_id?.let { it1 -> getLocationPhotos(it1, apiKey) }
                it.imageUrl= photos?.data?.get(0)?.images?.medium?.url.toString()

                var details = it.location_id?.let { it1 -> getLocationDetails(it1, apiKey) }
                it.url = details?.web_url
                it.latitude = details?.latitude
                it.longitude = details?.longitude
                it.location_details = details
            }
        }
        return locations
    }

    suspend fun getLocationPhotos(location_id: String, apiKey: String): LocationPhotosResponse {
        return locationsService.getLocationPhotos(location_id, apiKey)
    }

    suspend fun getLocationDetails(location_id: String, apiKey: String, ): LocationDetails {
        return locationsService.getLocationDetails(location_id, apiKey)
    }
}