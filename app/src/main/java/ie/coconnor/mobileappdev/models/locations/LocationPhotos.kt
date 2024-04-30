package ie.coconnor.mobileappdev.models.locations

import com.google.gson.annotations.SerializedName

data class LocationPhotosResponse(
    @SerializedName("data")
    val data: List<LocationPhoto>
)

data class LocationPhoto(
    val album: String,
    val caption: String,
    val id: Int,
    val images: Images,
    val is_blessed: Boolean,
    val published_date: String,
    val source: Source,
    val user: User
)

data class Images(
    val large: Large,
    val medium: Medium,
    val original: Original?,
    val small: Small,
    val thumbnail: Thumbnail
)

data class Source(
    val localized_name: String,
    val name: String
)

data class User(
    val username: String
)

data class Large(
    val height: Int,
    val url: String,
    val width: Int
)

data class Medium(
    val height: Int,
    val url: String,
    val width: Int
)

data class Original(
    val height: Int,
    val url: String,
    val width: Int
)

data class Small(
    val height: Int,
    val url: String,
    val width: Int
)

data class Thumbnail(
    val height: Int,
    val url: String,
    val width: Int
)
