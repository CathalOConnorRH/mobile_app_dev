package ie.coconnor.mobileappdev.repository

import ie.coconnor.mobileappdev.models.tour.TourResponse
import ie.coconnor.mobileappdev.service.RetrofitInstance
import ie.coconnor.mobileappdev.service.TourService

class TourRepository {
    private val tourService = RetrofitInstance.tourService

    suspend fun getTours(): TourResponse {
        return tourService.getTours()
    }
}