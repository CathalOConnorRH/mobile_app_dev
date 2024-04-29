package ie.coconnor.mobileappdev.ui.plan

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.Constants.Geofencing.RADIUS
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.models.plan.PlanViewModel
import ie.coconnor.mobileappdev.receiver.GeofenceBroadcastReceiver
import ie.coconnor.mobileappdev.repository.Trip
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.utils.SharedPref
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(
    viewModel: PlanViewModel,
    navController: NavController,
    sharedPref: SharedPref,
    geofencingClient: GeofencingClient)
{
    val trips by viewModel.trips.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    val tripName = remember { mutableStateOf("") }
    var showPopup by rememberSaveable { mutableStateOf(false) }

    val geofenceList = mutableListOf<Geofence>()
    val context = LocalContext.current
    val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }


    LaunchedEffect(Unit) {
        viewModel.fetchTrips()
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showPopup = true
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                shape = CircleShape,

                ) {
                Icon(Icons.Filled.Map, "Map View.")
                Text("Map View")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            PopupBox(
                popupWidth = 500F,
                popupHeight = 600F,
                showPopup = showPopup,
                onClickOutside = { showPopup = false },
                content = {
                   val boundsBuilder = LatLngBounds.builder()
                   for (trip in trips!!){
                       val location = LatLng(trip.location?.latitude?.toDoubleOrNull()!!, trip.location?.longitude?.toDoubleOrNull()!!)
                       boundsBuilder.include(location)
                   }
                    val bounds = boundsBuilder.build()
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(bounds.center, 14f)
                    }

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                        ){
                        for (trip in trips!!) {
                            val location = LatLng(trip.location?.latitude?.toDoubleOrNull()!!, trip.location?.longitude?.toDoubleOrNull()!!)
                            Marker(
                                state = MarkerState(position = location),
                                title = trip.location?.name,
                                snippet = "${trip.location?.location_details?.description}"
                            )
                        }
                    }
                }
            )
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
                OutlinedButton(
                    onClick = {
                        println("Start geofence")

                        for(trip in trips!!){
                            createGeofence(
                                geofenceList,
                                context,
                                geofencePendingIntent,
                                geofencingClient,
                                trip.location?.location_id.toString(),
                                trip.location?.latitude?.toDoubleOrNull()!!,
                                trip.location?.longitude?.toDoubleOrNull()!!
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,

                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "Start Tour"
                    )

                    Text(
                        text = "Start Tour",
                        modifier = Modifier.padding(6.dp),
                        color = Color.Black.copy()
                    )
                }
            }

            if (DataProvider.isAuthenticated && !DataProvider.isAnonymous) {

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
                                    navController.navigate(Destinations.LocationsScreen.route)
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
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
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
                }
            } else {
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
    }

//    fun getGeofenceRequest(): GeofencingRequest {
//        return GeofencingRequest.Builder().apply {
//            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            addGeofences(geofenceList)
//        }.build()
//    }
//
//    @SuppressLint("MissingPermission")
//    fun addGeofenceRequest() {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        geofencingClient.addGeofences(getGeofenceRequest(), geofencePendingIntent).run {
//            addOnSuccessListener {
//                Toast.makeText(
//                    context,
//                    "Geofence is added successfully",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            addOnFailureListener {
//                Timber.tag("TAG").e("Error ${it.localizedMessage}")
//                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    fun createGeofence(requestId: String, latitude: Double, longitude: Double){
//        geofenceList.add(
//            Geofence.Builder()
//                .setRequestId(requestId)
//                .setCircularRegion(latitude, longitude, RADIUS)
//                .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
//                .build()
//        )
//        addGeofenceRequest()
//
//    }
}

fun getGeofenceRequest(geofenceList: MutableList<Geofence>): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        addGeofences(geofenceList)
    }.build()
}

@SuppressLint("MissingPermission")
fun addGeofenceRequest(context: Context, geofencingClient: GeofencingClient, geofencePendingIntent: PendingIntent, geofenceList: MutableList<Geofence>) {

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        println("Missing permissions")
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    geofencingClient.addGeofences(getGeofenceRequest(geofenceList), geofencePendingIntent).run {
        addOnSuccessListener {
            Toast.makeText(
                context,
                "Geofence is added successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
        addOnFailureListener {
            Timber.tag("TAG").e("Error ${it.localizedMessage}")
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}

fun createGeofence(geofenceList: MutableList<Geofence>, context: Context, geofencePendingIntent: PendingIntent, geofencingClient: GeofencingClient, requestId: String, latitude: Double, longitude: Double){
    geofenceList.add(
//        Geofence.Builder()
//            .setRequestId(requestId)
//            .setCircularRegion(latitude, longitude, RADIUS)
//            .setExpirationDuration(Geofence.NEVER_EXPIRE)
//            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
//            .build()
        Geofence.Builder()
                .setRequestId(requestId)
                .setCircularRegion(latitude, longitude, RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
    )
    addGeofenceRequest(context, geofencingClient ,geofencePendingIntent, geofenceList )

}
//@Preview
//@Composable
//fun PlanScreenPreview(
//    viewModel: PlanViewModel = hiltViewModel(),
//    navController: NavController = rememberNavController(),
//    sharedPref: SharedPref? = null,
//    geofencingClient: GeofencingClient
//) {
//    if (sharedPref != null) {
//        PlanScreen(viewModel, navController, sharedPref, geofencingClient)
//    }
//}
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
    ElevatedCard(
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 36.dp
        ),
        modifier = modifier,
        onClick = {
            println("Clicked " + trip.location?.location_id)
        }
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
//                    .height(72.dp)
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
                    Text(text = trip.location?.name.toString(), style = MaterialTheme.typography.headlineMedium)

                }
            }

            Row(Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = trip.location?.location_details?.description.toString(),
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


@Composable
fun PopupBox(popupWidth: Float, popupHeight:Float, showPopup: Boolean, onClickOutside: () -> Unit, content: @Composable() () -> Unit) {

    if (showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10F),
            contentAlignment = Alignment.Center
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                ),
                onDismissRequest = { onClickOutside() }
            ) {
                Box(
                    Modifier
                        .width(popupWidth.dp)
                        .height(popupHeight.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }
    }
}