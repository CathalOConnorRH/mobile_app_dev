package ie.coconnor.mobileappdev

import retrofit2.http.Url

data class Attraction(
    val name: String,
    val lat: String,
    val long: String,
    val description: String,
    val image: String
)
