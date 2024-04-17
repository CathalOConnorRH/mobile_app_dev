package ie.coconnor.mobileappdev.ui.plan

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.models.plan.PlanViewModel
import ie.coconnor.mobileappdev.repository.Trip
import ie.coconnor.mobileappdev.ui.component.CustomDialog
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.utils.SharedPref

@Composable
fun PlanScreen(
    viewModel: PlanViewModel,
    navController: NavController,
    sharedPref: SharedPref)
{
    val trips by viewModel.trips.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    val tripName = remember { mutableStateOf("") }

    if(showDialog) {
        CustomDialog(value = "", title = "Create a trip", setShowDialog = {
            showDialog = it

        }) {

            tripName.value = it
            println(tripName)
//            viewModel.createOrUpdateTrip(tripName.value)
            Log.i("PlanScreen", "PlanScreen : ${it.toString()}")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchTrips()
    }

    Scaffold(
        floatingActionButton = {
            if(!trips.isNullOrEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    shape = CircleShape,

                    ) {
                    Icon(Icons.Filled.Create, "Search for new location.")
                    Text("Create a trip")
                }
            }
        }
    ) { paddingValues ->
        if (DataProvider.isAuthenticated) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Plans",
                        modifier = Modifier
                            .padding(10.dp),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if(trips.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "Favourite"
                        )

                        Text(
                            text = "Save your locations you'd like to visit",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Show saves on map"
                        )
                        Text(
                            text = "See your locations on a map",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        OutlinedButton(
                            onClick = {
                                showDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Text(
                                text = "+\tCreate a new trip",
                                modifier = Modifier.padding(6.dp),
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            } else {
                LazyColumn {
                    trips?.let {
                        items(it) { trip ->
                            //Text(text = tour.name)
                            StandardPlanCard(
                                trip = trip,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                            Spacer(modifier = Modifier.height(10.dp)) // Add a divider between items
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .padding(30.dp)
                        .background(
                            color = Color.LightGray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .height(150.dp)

                ) {
                    Column(
                        modifier = Modifier,
                    ) {

                        Row(
                            modifier = Modifier
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Log in to manage your tours and easily plan your next tour",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(Destinations.LoginScreen.route)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,

                                    )
                            ) {
                                Text(
                                    text = "Sign In",
                                    modifier = Modifier.padding(6.dp),
                                    color = Color.Black.copy()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PlanScreenPreview(
    viewModel: PlanViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref? = null
) {
    if (sharedPref != null) {
        PlanScreen(viewModel, navController, sharedPref)
    }
}
//@Composable
//fun TripList(
//    trips: Trip
//){
//    LazyColumn {
//        trips?.let {
//            items(it) { trip ->
//                //Text(text = tour.name)
//                StandardPlanCard(
//                    trip = trip,
//                    navController = navController,
//                    sharedPref = sharedPref
//                )
//                Spacer(modifier = Modifier.height(10.dp)) // Add a divider between items
//            }
//        }
//    }
//}
@Composable
fun StandardPlanCard(
    trip: Trip,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    background: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(background),
    shape: CornerBasedShape = MaterialTheme.shapes.extraLarge,
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref? = null,
    viewModel: PlanViewModel = hiltViewModel()

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
                    Text(text = trip.name.toString(), style = MaterialTheme.typography.headlineMedium)

                }
            }
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(location.imageUrl)
//                    .crossfade(true)
//                    .diskCacheKey(location.imageUrl)
//                    .memoryCacheKey(location.imageUrl)
//                    .error(placeholder)
//                    .fallback(placeholder)
//                    .diskCachePolicy(CachePolicy.ENABLED)
//                    .memoryCachePolicy(CachePolicy.ENABLED)
//                    .build(),
//                placeholder = painterResource(placeholder),
//                contentDescription = "",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .background(color = MaterialTheme.colorScheme.secondary)
//                    .fillMaxWidth()
//                    .height(250.dp)
//            )
            Row(Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = trip.toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {


            }
        }
    }
}