package ie.coconnor.mobileappdev.models.locations

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("data")
    val data: List<Location>
)

data class LocationDetailsResponse(
    @SerializedName("location")
    val location: LocationDetails
)

data class Location(
    var location_id: String? = "",
    var name: String,
    var address_obj: Address? = null,
    var imageUrl: String? = "",
    var url: String? = "",
    var latitude: String? = "",
    var longitude: String? = "",
    var location_details: LocationDetails? = null,
    var saved: Boolean? = false

){
    constructor(): this("","")

}

data class Address(
    val street1: String? = "",
    val street2: String? = "",
    var city: String? = "",
    val state: String? = "",
    val country: String? = "",
    val postalcode: String? = "",
    val address_string: String? = ""
)


data class LocationDetails(
    val address_obj: AddressObj? = null,
    val ancestors: List<Ancestor>? = null,
    val awards: List<Award>? = null,
    val category: Category? = null,
    val description: String? = null,
    val email: String? = null,
    val groups: List<Group>? = null,
    val hours: Hours? = null,
    val latitude: String? = null,
    val location_id: String? = null,
    val longitude: String? = null,
    val name: String? = null,
    val neighborhood_info: List<Any>? = null,
    val num_reviews: String? = null,
    val phone: String? = null,
    val photo_count: String? = null,
    val ranking_data: RankingData? = null,
    val rating: String? = null,
    val rating_image_url: String? = null,
    val review_rating_count: ReviewRatingCount? = null,
    val see_all_photos: String? = null,
    val subcategory: List<Subcategory>? = null,
    val timezone: String? = null,
    val trip_types: List<TripType>? = null,
    val web_url: String? = null,
    val website: String? = null,
    val write_review: String? = null
) {
    data class AddressObj(
        val address_string: String? = null,
        val city: String? = null,
        val country: String? = null,
        val postalcode: String? = null,
        val state: String? = null,
        val street1: String? = null,
        val street2: String? = null
    )

    data class Ancestor(
        val level: String? = null,
        val location_id: String? = null,
        val name: String? = null
    )

    data class Award(
        val award_type: String? = null,
        val categories: List<Any>? = null,
        val display_name: String? = null,
        val images: Images? = null,
        val year: String? = null
    ) {
        data class Images(
            val large: String? = null,
            val small: String? = null,
            val tiny: String? = null
        )
    }

    data class Category(
        val localized_name: String? = null,
        val name: String? = null
    )

    data class Group(
        val categories: List<Category>? = null,
        val localized_name: String? = null,
        val name: String? = null
    ) {
        data class Category(
            val localized_name: String? = null,
            val name: String? = null
        )
    }

    data class Hours(
        val periods: List<Period>? = null,
        val weekday_text: List<String>? = null
    ) {
        data class Period(
            val close: Close? = null,
            val `open`: Open? = null
        ) {
            data class Close(
                val day: Int? = null,
                val time: String? = null
            )

            data class Open(
                val day: Int? = null,
                val time: String? = null
            )
        }
    }

    data class RankingData(
        val geo_location_id: String? = null,
        val geo_location_name: String? = null,
        val ranking: String? = null,
        val ranking_out_of: String? = null,
        val ranking_string: String? = null
    )

    data class ReviewRatingCount(
        val `1`: String? = null,
        val `2`: String? = null,
        val `3`: String? = null,
        val `4`: String? = null,
        val `5`: String? = null
    )

    data class Subcategory(
        val localized_name: String? = null,
        val name: String? = null
    )

    data class TripType(
        val localized_name: String? = null,
        val name: String? = null,
        val value: String? = null
    )
}