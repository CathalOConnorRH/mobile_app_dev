package ie.coconnor.mobileappdev.models

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
    var location_id: String,
    var name: String,
    var address_obj: Address? = null,
    var imageUrl: String? = "",
    var url: String? = "",
    var latitude: String? = "",
    var longitude: String? = ""
)

data class Address(
    val street1: String? = null,
    val street2: String? = null,
    var city: String,
    val state: String,
    val country: String,
    val postalcode: String? = null,
    val address_string: String
)


data class LocationDetails(
    val address_obj: AddressObj,
    val ancestors: List<Ancestor>,
    val awards: List<Award>,
    val category: Category,
    val description: String,
    val email: String,
    val groups: List<Group>,
    val hours: Hours,
    val latitude: String,
    val location_id: String,
    val longitude: String,
    val name: String,
    val neighborhood_info: List<Any>,
    val num_reviews: String,
    val phone: String,
    val photo_count: String,
    val ranking_data: RankingData,
    val rating: String,
    val rating_image_url: String,
    val review_rating_count: ReviewRatingCount,
    val see_all_photos: String,
    val subcategory: List<Subcategory>,
    val timezone: String,
    val trip_types: List<TripType>,
    val web_url: String,
    val website: String,
    val write_review: String
) {
    data class AddressObj(
        val address_string: String,
        val city: String,
        val country: String,
        val postalcode: String,
        val state: String,
        val street1: String,
        val street2: String
    )

    data class Ancestor(
        val level: String,
        val location_id: String,
        val name: String
    )

    data class Award(
        val award_type: String,
        val categories: List<Any>,
        val display_name: String,
        val images: Images,
        val year: String
    ) {
        data class Images(
            val large: String,
            val small: String,
            val tiny: String
        )
    }

    data class Category(
        val localized_name: String,
        val name: String
    )

    data class Group(
        val categories: List<Category>,
        val localized_name: String,
        val name: String
    ) {
        data class Category(
            val localized_name: String,
            val name: String
        )
    }

    data class Hours(
        val periods: List<Period>,
        val weekday_text: List<String>
    ) {
        data class Period(
            val close: Close,
            val `open`: Open
        ) {
            data class Close(
                val day: Int,
                val time: String
            )

            data class Open(
                val day: Int,
                val time: String
            )
        }
    }

    data class RankingData(
        val geo_location_id: String,
        val geo_location_name: String,
        val ranking: String,
        val ranking_out_of: String,
        val ranking_string: String
    )

    data class ReviewRatingCount(
        val `1`: String,
        val `2`: String,
        val `3`: String,
        val `4`: String,
        val `5`: String
    )

    data class Subcategory(
        val localized_name: String,
        val name: String
    )

    data class TripType(
        val localized_name: String,
        val name: String,
        val value: String
    )
}