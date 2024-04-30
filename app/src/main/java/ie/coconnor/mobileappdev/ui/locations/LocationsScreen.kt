package ie.coconnor.mobileappdev.ui.locations

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.locations.Address
import ie.coconnor.mobileappdev.models.locations.Location
import ie.coconnor.mobileappdev.models.locations.LocationsViewModel
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.utils.SharedPref

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LocationsScreen(viewModel: LocationsViewModel,
                    navController: NavHostController,
                    sharedPref: SharedPref)
{
    val locations by viewModel.locations.observeAsState()
    val trips by viewModel.trips.observeAsState()

    var location by remember { mutableStateOf("Waterford, Ireland") }

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val tripAdvisorApiKey = context.getString(R.string.tripadvisor)

    LaunchedEffect(Unit) {
        viewModel.fetchTrips()
        viewModel.fetchTours(location, tripAdvisorApiKey)
    }
    Scaffold(
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = {showDialog = true },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                shape = CircleShape,

                ) {
                Icon(Icons.Filled.Search, "Search for new location.")
            }
        }
    ) {
        Column {
            if (locations?.data?.isEmpty() == true) {
                // Show loading indicator or placeholder
                Text(text = "Loading...")
            } else {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = { Text("Search new destination") },
                        text = {
                            TextField(
                                value = location,
                                onValueChange = { newText ->
                                    location = newText
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    viewModel.fetchTours(location, tripAdvisorApiKey)
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                // Display the list of Locations
                    LazyColumn {
                        locations?.let {
                            items(it.data) { location ->
                                var saved = false
                                println("Trips ${trips?.size.toString()}")
                                trips?.forEach { trip ->
                                    println(trip.location?.location_id.toString())
                                    if(trip.location?.location_id?.contains(location.location_id.toString()) == true) {
                                        println(location.name)
                                        location.saved = true
                                        saved = true
                                    }
                                }

                                StandardLocationCard(
                                    location = location,
                                    navController = navController,
                                    sharedPref = sharedPref,
                                    saved = saved
                                )
                                Spacer(modifier = Modifier.height(10.dp)) // Add a divider between items
                            }
                        }
                    }
            }
        }
    }
}

@Preview(group = "Locations")
@Composable
fun PreviewStandCardItem(
    @PreviewParameter(SampleLocationProvider::class) location: Location,
){
//    StandardLocationCard(location = location, trips)
}

@Composable
fun StandardLocationCard(
    location: Location,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    background: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(background),
    shape: Shape = MaterialTheme.shapes.extraLarge,
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref? = null,
    viewModel: LocationsViewModel = hiltViewModel(),
    saved: Boolean? = false
    ) {
    val placeholder = R.drawable.vector
    val showTripDialog = false
    ElevatedCard(
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 36.dp
        ),
        modifier = modifier
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
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
                    Text(text = location.name, style = typography.headlineMedium)

                }
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(location.imageUrl)
                    .crossfade(true)
                    .diskCacheKey(location.imageUrl)
                    .memoryCacheKey(location.imageUrl)
                    .error(placeholder)
                    .fallback(placeholder)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                placeholder = painterResource(placeholder),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Row(Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = location.address_obj?.address_string.toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.bodyMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {

                Box(
                    Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {

                    Row(modifier = Modifier.align(Alignment.CenterStart)) {

                        TextButton(onClick = {
                            navController.navigate(Destinations.LocationDetailsScreen.route + "/${location.location_id}")
                        }) {
                            Text(text = "More Details")
                        }
                    }
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(onClick = {
                            viewModel.createTrip(location)
                            navController.navigate(Destinations.PlanScreen.route)
                        }) {
                            if(location.saved == true) {
                                Icon(Icons.Default.Favorite, contentDescription = null)
                            } else {
                                Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                            }
                        }

                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, location.url)
                            putExtra(Intent.EXTRA_TITLE, "Check out this location I want to visit")
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        val context = LocalContext.current

                        IconButton(onClick = {
                            startActivity(context, shareIntent, null)
                        }) {
                            Icon(Icons.Default.Share, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

class SampleLocationProvider : PreviewParameterProvider<Location>{
    override val values = sequenceOf(
        Location(
            name = "Waterford Treasures",
            location_id = "1",
            address_obj = Address("High Street", "", "", "", "", "", "Street 1")
        ),
        Location(
            name = "Waterford Death Museum",
            location_id = "2",
            address_obj = Address("High Street", "", "", "", "", "", "Street 2")
        ),
        Location(
            name = "Reginald's Tower",
            location_id = "3",
            address_obj = Address("High Street", "", "", "", "", "", "Street 3")
        )
    )

}