package ie.coconnor.mobileappdev.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ie.coconnor.mobileappdev.models.Address
import ie.coconnor.mobileappdev.models.Location
import ie.coconnor.mobileappdev.models.tour.TourViewModel

@Composable
fun TourScreen(viewModel: TourViewModel) {
    val tours by viewModel.tours.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTours()
    }

    Column {
        if (tours?.data?.isEmpty() == true) {
            // Show loading indicator or placeholder
            Text(text = "Loading...")
        } else {
            // Display the list of credit cards
            LazyColumn {
                tours?.let {
                    items(it.data) { tour ->
                        //Text(text = tour.name)
                        TourItem(tour = tour)
                        HorizontalDivider() // Add a divider between items
                    }
                }
            }
        }
    }
}


@Preview(group = "Locations")
@Composable
fun PreviewTourItem(@PreviewParameter(SampleLocationProvider::class) location: Location){
//    var address = Address("High Street", "", "", "", "", "", "")

//    location.address_obj = address
            TourItem(tour = location)
}

class SampleLocationProvider : PreviewParameterProvider<Location> {
    override val values = sequenceOf(
        Location(
            name = "Waterford Treasures",
            location_id = "1",
            address_obj = Address("High Street", "", "", "", "", "", "High Street")
        ),
        Location(
            name = "Waterford Death Museum",
            location_id = "2",
            address_obj = Address("High Street", "", "", "", "", "", "High Street")
        ),
        Location(
            name = "Reginalds Tower",
            location_id = "3",
            address_obj = Address("High Street", "", "", "", "", "", "High Street")
        )
    )
}

@Composable
fun TourItem(tour: Location) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = tour.name,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Location Id: ${tour.location_id}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tour.address_obj?.address_string.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
