package ie.coconnor.mobileappdev

//import com.google.firebase.firestore.FirebaseFirestore
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import ie.coconnor.mobileappdev.models.AuthState
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.models.locations.LocationDetailsViewModel
import ie.coconnor.mobileappdev.models.locations.LocationsViewModel
import ie.coconnor.mobileappdev.models.plan.PlanViewModel
import ie.coconnor.mobileappdev.service.LocationForegroundService
import ie.coconnor.mobileappdev.ui.login.LoginScreen
import ie.coconnor.mobileappdev.ui.login.SignUpScreen
import ie.coconnor.mobileappdev.ui.navigation.BottomBar
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.ui.plan.PlanScreen
import ie.coconnor.mobileappdev.ui.screens.SettingsScreen
import ie.coconnor.mobileappdev.ui.screens.TestScreen
import ie.coconnor.mobileappdev.ui.screens.locations.LocationDetailsScreen
import ie.coconnor.mobileappdev.ui.screens.locations.LocationsScreen
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme
import ie.coconnor.mobileappdev.utils.SharedPref
import ie.coconnor.mobileappdev.utils.UIThemeController
import javax.inject.Inject
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.ui.graphics.Color
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import ie.coconnor.mobileappdev.receiver.GeofenceBroadcastReceiver

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPref: SharedPref

    val authViewModel by viewModels<AuthViewModel>()
    val tourViewModel by viewModels<LocationsViewModel>()
    val locationDetailsViewModel by viewModels<LocationDetailsViewModel> ()
    val planViewModel by viewModels<PlanViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var geofencingClient: GeofencingClient
//    private val geofenceList = mutableListOf<Geofence>()
//
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }


//    lateinit var textToSpeech: TextToSpeech


    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {

        }

        geofencingClient = LocationServices.getGeofencingClient(this)
        WindowCompat.setDecorFitsSystemWindows(window, true)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//
//        )
//        window.setTitle("Test")
        //createLocationRequest()
       // createGeofence()
//        println(sharedPref.getDarkMode())
        UIThemeController.updateUITheme(sharedPref.getDarkMode())

        setContent {

            val isDarkMode by UIThemeController.isDarkMode.collectAsState()
            MobileAppDevTheme (darkTheme = isDarkMode){

                val navController = rememberNavController()
                var buttonsVisible = remember { mutableStateOf(true) }

                val currentUser = authViewModel.currentUser.collectAsState().value
                DataProvider.updateAuthState(currentUser)

                Log.i("AuthRepo", "Authenticated: ${DataProvider.isAuthenticated}")
                Log.i("AuthRepo", "Anonymous: ${DataProvider.isAnonymous}")
                Log.i("AuthRepo", "User: ${DataProvider.user}")

                val multiplePermission = rememberMultiplePermissionsState(
                    permissions = listOf(
                        android.Manifest.permission.POST_NOTIFICATIONS,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION

                    )
                )
                val context = LocalContext.current
                val showRationalDialog = remember { mutableStateOf(false) }

                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        // Permission granted
                    } else {
                        // Handle permission denial
                    }
                }

                if (showRationalDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            showRationalDialog.value = false
                        },
                        title = {
                            Text(
                                text = "Permission",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        },
                        text = {
                            Text(
                                if (multiplePermission.revokedPermissions.size == 4) {
                                    "We need some permissions for geofencing activities"
                                } else if (multiplePermission.revokedPermissions.isNotEmpty() && multiplePermission.revokedPermissions.first().permission == android.Manifest.permission.ACCESS_FINE_LOCATION) {
                                    "We need fine location permission. Please grant the permission."
                                } else if (multiplePermission.revokedPermissions.isNotEmpty() && multiplePermission.revokedPermissions.first().permission == android.Manifest.permission.ACCESS_BACKGROUND_LOCATION){
                                    "We need background location permission. Please grant the permission to be allowed all the time."
                                } else if (multiplePermission.revokedPermissions.isNotEmpty() && multiplePermission.revokedPermissions.first().permission == android.Manifest.permission.POST_NOTIFICATIONS) {
                                    "We need to send notifications."
                                } else if (multiplePermission.revokedPermissions.isNotEmpty()) {
                                    "We need ${multiplePermission.revokedPermissions.first().permission.toString()} permission. Please grant the permission."
                                } else {
                                    showRationalDialog.value = false
                                    ""
                                },
                                fontSize = 16.sp
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showRationalDialog.value = false
                                    if(multiplePermission.revokedPermissions.isNotEmpty()) {
                                        requestPermissionLauncher.launch(multiplePermission.revokedPermissions.first().permission)
                                    }
                                }) {
                                Text("OK", style = TextStyle(color = Color.Black))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showRationalDialog.value = false
                                }) {
                                Text("Cancel", style = TextStyle(color = Color.Black))
                            }
                        },
                    )
                }

                Scaffold(
                        bottomBar = {
                            BottomBar(
                                navController = navController,
                                state = buttonsVisible,
                                modifier = Modifier
                            )
                        }) { paddingValues ->
                    if (multiplePermission.shouldShowRationale) {
                        // Show a rationale if needed (optional)
                        showRationalDialog.value = true
                    } else {
                        // Request the permission
                        if(!multiplePermission.revokedPermissions.isEmpty())
                        requestPermissionLauncher.launch(multiplePermission.revokedPermissions.first().permission)

                    }
                        Box(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            NavigationGraph(navController = navController, authViewModel = authViewModel, tourViewModel = tourViewModel, locationDetailsViewModel = locationDetailsViewModel, planViewModel, sharedPref = sharedPref)
                        }
                    }
                }

            }
        }



//    //    @SuppressLint("MissingPermission")
//    private fun currentLocation() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
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
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                    val latlng = LatLng(location.latitude, location.longitude)
//                    println(latlng)
//
//            }
//        }
//    }


//    private fun createLocationRequest() {
//        val locationRequest = LocationRequest.create().apply {
//            interval = LOCATION_INTERVAL
//            fastestInterval = LOCATION_FASTEST_INTERVAL
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//
//
//        val client: SettingsClient = LocationServices.getSettingsClient(this)
//        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener {
//            Log.i("Location", " Enable Successful")
//
//        }
//        task.addOnFailureListener { exception ->
//            Log.i("Location", exception.message.toString())
//            if (exception is ResolvableApiException) {
//                try {
//                    exception.startResolutionForResult(
//                        this,
//                        REQUEST_CHECK_SETTINGS
//                    )
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    // Ignore the error.
//                    println(sendEx)
//                }
//            }
//        }
//    }
//    private fun createGeofence(){
//        geofenceList.add(
//            Geofence.Builder()
//                .setRequestId("entry.key")
//                .setCircularRegion(52.2598299, -7.1085459, RADIUS)
//                .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
//                .build()
//        )
//        addGeofenceRequest()
//
//    }
//    @SuppressLint("MissingPermission")
//    private fun addGeofenceRequest() {
//        geofencingClient.addGeofences(getGeofenceRequest(), geofencePendingIntent).run {
//            addOnSuccessListener {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Geofence is added successfully",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            addOnFailureListener {
//                Log.e("Error", it.localizedMessage)
//                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//    private fun getGeofenceRequest(): GeofencingRequest {
//        return GeofencingRequest.Builder().apply {
//            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            addGeofences(geofenceList)
//        }.build()
//    }
//
//    private fun checkBackGroundLocationPermission(): Boolean {
//        return  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            (ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED)
//        } else {
//            return true
//        }
//
//    }

//    private fun requestBackGroundLocationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED
//                ) {
//                    startLocationService()
//                } else {
//                    // Handle the case where the user denies the foreground service permission
//                }
//            }
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                    grantResults[1] == PackageManager.PERMISSION_GRANTED
//                ) {
//                    checkAndRequestLocationPermissions()
//                } else {
//                    // Handle the case where the user denies the location permission
//                }
//            }
//        }
//    }
//
//    private fun checkAndRequestLocationPermissions() {
//        if (checkLocationPermission()) {
//            if (checkBackGroundLocationPermission()){
//                startLocationService()
//            }else{
//                requestBackGroundLocationPermission()
//            }
//
//        } else {
//            requestLocationPermission()
//        }
//    }

//    private fun checkLocationPermission(): Boolean {
//        return (ContextCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(
//                    this,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED)
//    }

//    private fun requestLocationPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            LOCATION_PERMISSION_REQUEST_CODE
//        )
//    }

//    fun checkNotificationPermission() {
//        val permission = android.Manifest.permission.POST_NOTIFICATIONS
//        when {
//            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
//                // make your action here
//                checkAndRequestLocationPermissions()
//            }
//            shouldShowRequestPermissionRationale(permission) -> {
//                // permission denied permanently
//            }
//            else -> {
//                requestNotificationPermission.launch(permission)
//            }
//        }
//    }

//    private val requestNotificationPermission =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted->
//            if (isGranted) // make your action here
//                checkAndRequestLocationPermissions()
//        }
    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        startService(serviceIntent)
    }
    fun stopLocationService() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        stopService(serviceIntent)
    }
}


@Composable
fun NavigationGraph(navController: NavHostController,
                    authViewModel: AuthViewModel,
                    tourViewModel: LocationsViewModel,
                    locationDetailsViewModel: LocationDetailsViewModel,
                    planViewModel: PlanViewModel,
                    sharedPref: SharedPref) {
    var startDestination = Destinations.TestScreen.route

    if (DataProvider.authState == AuthState.SignedOut)
    {
        startDestination = Destinations.LoginScreen.route
    }
    NavHost(navController, startDestination = startDestination) {
        composable(Destinations.LoginScreen.route) {
            LoginScreen( authViewModel)
        }
        composable(Destinations.Favourite.route) {
            SignUpScreen(navController)
        }
        composable(Destinations.LocationsScreen.route) {
            LocationsScreen(tourViewModel, navController , sharedPref)
        }
        composable(Destinations.PlanScreen.route) {
            PlanScreen(planViewModel, navController, sharedPref)
        }
        composable(Destinations.TestScreen.route) {
            TestScreen(navController)
        }
        composable(Destinations.SettingsScreen.route) {
            SettingsScreen(navController, authViewModel)
        }
        composable(Destinations.LocationDetailsScreen.route) {
            LocationDetailsScreen(navController, locationDetailsViewModel,sharedPref)
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)

@Composable
fun MobileAppDevPreview() {
    MobileAppDevTheme {

    }
}
