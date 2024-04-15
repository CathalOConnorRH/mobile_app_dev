package ie.coconnor.mobileappdev.ui.screens.locations

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import ie.coconnor.mobileappdev.models.locations.LocationDetailsViewModel
import ie.coconnor.mobileappdev.utils.SharedPref

@Composable
fun LocationDetailsScreen(
    navController: NavHostController,
    viewModel: LocationDetailsViewModel,
    sharedPref: SharedPref
) {
    val locationDetails by viewModel.locationDetails.observeAsState()

    LaunchedEffect(Unit) {
        val location_id = sharedPref.getLocationId()
        println(location_id)
        viewModel.fetchLocationDetails(location_id)
    }

    Column {
        if (locationDetails == null) {
            // Show loading indicator or placeholder
            Text(text = "Loading...")
        } else {
            // Display the list of credit cards
//            LazyColumn {
//                locationDetails?.let {
//                    items(it.data) { location ->
//                        //Text(text = tour.name)
//                        StandardCard(location = location, navController = navController)
//                        HorizontalDivider() // Add a divider between items
//                    }
//                }
//            }
            println("lOCATION details screen " + locationDetails.toString())
        }
    }
}