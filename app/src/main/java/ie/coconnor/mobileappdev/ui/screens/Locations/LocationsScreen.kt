package ie.coconnor.mobileappdev.ui.screens.Locations

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.Address
import ie.coconnor.mobileappdev.models.Location
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
    val location = sharedPref.getLocationToSearch()

    LaunchedEffect(Unit) {
        println(location)
        viewModel.fetchTours()
    }
    Scaffold(
    ) { paddingValues ->
        Column {
            if (locations?.data?.isEmpty() == true) {
                // Show loading indicator or placeholder
                Text(text = "Loading...")
            } else {
                // Display the list of credit cards
                LazyColumn {
                    locations?.let {
                        items(it.data) { location ->
                            //Text(text = tour.name)
                            StandardCard(
                                location = location,
                                navController = navController,
                                sharedPref = sharedPref
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
    @PreviewParameter(SampleLocationProvider::class) location: Location){
    StandardCard(location = location)
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
            name = "Reginalds Tower",
            location_id = "3",
            address_obj = Address("High Street", "", "", "", "", "", "Street 3")
        )
    )
}

@Composable
fun StandardCard(
    location: Location,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    background: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(background),
    shape: Shape = MaterialTheme.shapes.extraLarge,
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref? = null

) {
    val placeholder = R.drawable.vector

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
                // Miniatura
                Box(
                    modifier = Modifier
//                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                        .size(40.dp),
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
//                        text = LoremIpsum(50).values.take(10).joinToString(separator = " "),
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
                            sharedPref?.setLocationId(location.location_id.toString())
                            navController.navigate(Destinations.LocationDetailsScreen.route)
                        }) {
                            Text(text = "More Details")
                        }

//                        Spacer(modifier = Modifier.width(8.dp))
//
//                        TextButton(onClick = { /*TODO*/ }) {
//                            Text(text = "ACCIÓN 2")
//                        }
                    }

                    // Iconos
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Favorite, contentDescription = null)
                        }

                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, location.url)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        var context = LocalContext.current

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
