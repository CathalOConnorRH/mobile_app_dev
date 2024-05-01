package ie.coconnor.mobileappdev.ui.plan

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
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
import ie.coconnor.mobileappdev.models.Constants.Geofencing.RADIUS
import ie.coconnor.mobileappdev.models.auth.DataProvider
import ie.coconnor.mobileappdev.models.plan.PlanViewModel
import ie.coconnor.mobileappdev.receiver.GeofenceBroadcastReceiver
import ie.coconnor.mobileappdev.repository.Trip
import ie.coconnor.mobileappdev.ui.components.ExpandableCard
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import timber.log.Timber

@Composable
fun PlanScreen(
    viewModel: PlanViewModel,
    navController: NavController,
    geofencingClient: GeofencingClient)
{
    val TAG = "PlanScreen"
    val trips by viewModel.trips.observeAsState()
    var showPopup by rememberSaveable { mutableStateOf(false) }
    val tourButton = remember { mutableStateOf("Start Tour") }
    val geofenceList = mutableListOf<Geofence>()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchTrips()
    }

    Scaffold(
        floatingActionButton = {
            if (DataProvider.isAuthenticated && !DataProvider.isAnonymous) {
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
                if (DataProvider.isAuthenticated && !DataProvider.isAnonymous) {

                    OutlinedButton(
                        onClick = {
                            if (tourButton.value == "Start Tour") {
                                Timber.tag(TAG).i("Start geofence tour")

                                for (trip in trips!!) {
                                    createGeofence(
                                        geofenceList,
                                        context,
                                        geofencingClient,
                                        trip
                                    )
                                }
                                tourButton.value = "Stop Tour"
                            } else {
                                Timber.tag(TAG).i("Stop geofence tour")
                                removeGeofence(geofencingClient, context)
                                tourButton.value = "Start Tour"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (tourButton.value == "Start Tour") {
                            Icon(Icons.Filled.PlayArrow, "Start Tour.")
                        } else {
                            Icon(Icons.Filled.Stop, "Stop Tour.")
                        }
                        Text(
                            text = tourButton.value,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
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
                                    val delete = SwipeAction(
                                        onSwipe = {
                                            viewModel.onSwipeToDelete(trip.location?.location_id.toString())
                                            Timber.tag("TAG").i("OnSwipeToDelete ${trip.location?.location_id.toString()}")
                                        },
                                        icon = {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete chat",
                                                modifier = Modifier.padding(16.dp),
                                                tint = Color.White
                                            )
                                        }, background = Color.Red.copy(alpha = 0.5f),
                                        isUndo = true
                                    )
                                    SwipeableActionsBox(
                                        modifier = Modifier,
                                        swipeThreshold = 200.dp,
                                        endActions = listOf(delete)
                                    ) {
                                        ExpandableCard(
                                            title = trip.location?.name.toString(),
                                            description = trip.location?.location_details?.description.toString()
                                        )
                                    }
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
}

fun getGeofenceRequest(geofenceList: MutableList<Geofence>): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        addGeofences(geofenceList)
    }.build()
}

@SuppressLint("MissingPermission")
fun addGeofenceRequest(context: Context, geofencingClient: GeofencingClient, geofencePendingIntent: PendingIntent, geofenceList: MutableList<Geofence>) {
    val TAG = "addGeofenceRequest"
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Timber.tag(TAG).e("Missing permissions")
        return
    }
    geofencingClient.addGeofences(getGeofenceRequest(geofenceList), geofencePendingIntent).run {
        addOnSuccessListener {
            Toast.makeText(
                context,
                "Geofence is added successfully",
                Toast.LENGTH_SHORT
            ).show()
            Timber.tag(TAG).i("Geofence added")

        }
        addOnFailureListener {
            Timber.tag(TAG).e("Error ${it.localizedMessage}")
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}

fun removeGeofence(geofencingClient: GeofencingClient, context: Context) {
    val TAG = "removeGeofence"
    val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
    val geofencePendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )
    geofencingClient.removeGeofences(geofencePendingIntent).run {
        addOnSuccessListener {
            Timber.tag(TAG).i("Geofence Removed")
        }
        addOnFailureListener {
            Timber.tag(TAG).e("Error ${it.localizedMessage}")
        }
    }
}
fun createGeofence(geofenceList: MutableList<Geofence>, context: Context, geofencingClient: GeofencingClient, trip: Trip){

    val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        .putExtra("tripName", trip.location?.name)
        .putExtra("tripDescription", trip.location?.location_details?.description)
        .putExtra("message", "Geofence alert - ${trip.location?.latitude}, ${trip.location?.longitude}")

    val geofencePendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )


    geofenceList.add(
        Geofence.Builder()
                .setRequestId(trip.location?.location_id.toString())
                .setCircularRegion(trip.location?.latitude?.toDoubleOrNull()!!, trip.location?.longitude?.toDoubleOrNull()!!, RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
    )
    addGeofenceRequest(context, geofencingClient ,geofencePendingIntent, geofenceList )

}

@Composable
fun PopupBox(popupWidth: Float, popupHeight:Float, showPopup: Boolean, onClickOutside: () -> Unit, content: @Composable () -> Unit) {

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