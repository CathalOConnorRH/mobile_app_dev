package ie.coconnor.mobileappdev.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.coconnor.mobileappdev.models.tour.TourResponse
import ie.coconnor.mobileappdev.models.tour.TourViewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun TourScreen(viewModel: TourViewModel) {
    val tours by viewModel.tours.observeAsState(null)

    LaunchedEffect(Unit) {
        viewModel.fetchTours()
    }

    Column {
        if (tours == null) {
            // Show loading indicator or placeholder
            Text(text = "Loading...")
        } else {
            // Display the list of credit cards
            TourItem(tours!!)
        }
    }
}


@Composable
fun TourItem(tour: TourResponse) {
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
                text = tour.location_id,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Expiry Date: ${tour.location_id}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tour.location_id,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}