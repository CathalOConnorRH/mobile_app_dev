package ie.coconnor.mobileappdev.service

import ie.coconnor.mobileappdev.models.tour.TourResponse
import ie.coconnor.mobileappdev.models.tour.Tour

import retrofit2.http.GET

interface TourService{
    @GET("tours")
    suspend fun getTours(): TourResponse
}