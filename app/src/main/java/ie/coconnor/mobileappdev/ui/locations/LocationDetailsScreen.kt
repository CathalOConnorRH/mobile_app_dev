package ie.coconnor.mobileappdev.ui.locations

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.locations.LocationDetails
import ie.coconnor.mobileappdev.models.locations.LocationDetailsViewModel
import ie.coconnor.mobileappdev.ui.components.UserRatingBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LocationDetailsScreen(
    viewModel: LocationDetailsViewModel,
    location: String?
) {
    val locationDetails by viewModel.locationDetails.observeAsState()
    val context = LocalContext.current
    val tripAdvisorApiKey = context.getString(R.string.tripadvisor)
    LaunchedEffect(Unit) {
        if (location != null) {
            viewModel.fetchLocationDetails(location, tripAdvisorApiKey)
        }
    }

    Scaffold()
    {
        Column {
            if (locationDetails == null) {
                Text(text = "Loading...")
            } else {
                LocationDetailsDisplay(locationDetails!!)
            }
        }
    }
}

@Composable
fun LocationDetailsDisplay(
    locationDetails: LocationDetails,
    modifier: Modifier = Modifier
){

    ElevatedCard(
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 36.dp
        ),
        modifier = modifier
    ) {
        Column {
            Row(
                modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier
                        .size(40.dp)
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.vector),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                Column(Modifier.fillMaxWidth()) {
                    Text(text = locationDetails.name.toString(), style = MaterialTheme.typography.headlineMedium)

                }
            }
            Row(Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = locationDetails.description.toString(),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)) {
                UserRatingBar(
                    // 2. Customized UserRatingBar
                    ratingState = (locationDetails.rating?.toFloat() ?: 0.0) as Float,
                    ratingIconPainter = painterResource(id = R.drawable.vector),
                    size = 34.dp,
                    selectedColor = Color(0xFF5A966E),
                )
                Text(
                    text = "Current Rating: ${locationDetails.rating?.toFloat() ?: 0.0}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            val locationOnMap = LatLng(locationDetails.latitude?.toDoubleOrNull()!!, locationDetails.longitude?.toDoubleOrNull()!!)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(locationOnMap, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxWidth(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = locationOnMap),
                    title = locationDetails.name,
                    snippet = locationDetails.description?.substring(0,20)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Preview(group = "LocationDetails")
@Composable
fun PreviewLocationsDetails(
    @PreviewParameter(SampleLocationDetailsProvider::class) locationDetails: LocationDetails
)
{
    LocationDetailsDisplay(locationDetails = locationDetails)
}

class SampleLocationDetailsProvider : PreviewParameterProvider<LocationDetails>{
    override val values = sequenceOf(
        LocationDetails(
            location_id = "10166976",
            name =  "Waterford Greenway",
            description= "Waterford Greenway is a spectacular 46 km off-road cycling and walking trail along an old railway line which runs between Waterford and Dungarvan. From the Viking City alongside the River Suir, out by Mount Congreve Gardens, through Kilmacthomas, across eleven bridges, over three impressive viaducts and through a 400m long tunnel; with wonderful views of the Comeragh Mountains and Dungarvan Bay.",
            web_url = "https://www.tripadvisor.com/Attraction_Review-g186638-d10166976-Reviews-Waterford_Greenway-Waterford_County_Waterford.html?m=66827",
            address_obj = LocationDetails.AddressObj("46 klm of old disused railway line now a cycle and pedestrian route between Waterford and Dungarvan.",
                        "Waterford","Province of Munster","country", "Ireland","","46 klm of old disused railway line now a cycle and pedestrian route between Waterford and Dungarvan., Waterford Ireland"
            ),
            latitude = "",
            longitude = "",
            ranking_data = LocationDetails.RankingData(ranking = "1", ranking_out_of = "47", ranking_string = "#1 of 47 things to do in Waterford"),
            rating = "5.0",
            rating_image_url = "https://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/5.0-66827-5.svg",
            num_reviews = "710",
            review_rating_count = LocationDetails.ReviewRatingCount("2","9", "17", "96","586" )
        )
    )
}

