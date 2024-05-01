package ie.coconnor.mobileappdev

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import ie.coconnor.mobileappdev.models.Constants.BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
import ie.coconnor.mobileappdev.models.Constants.Geofencing.LOCATION_FASTEST_INTERVAL
import ie.coconnor.mobileappdev.models.Constants.Geofencing.LOCATION_INTERVAL
import ie.coconnor.mobileappdev.models.Constants.Geofencing.REQUEST_CHECK_SETTINGS
import ie.coconnor.mobileappdev.models.Constants.Geofencing.REQUEST_CODE
import ie.coconnor.mobileappdev.models.Constants.LOCATION_PERMISSION_REQUEST_CODE
import ie.coconnor.mobileappdev.models.auth.AuthState
import ie.coconnor.mobileappdev.models.auth.AuthViewModel
import ie.coconnor.mobileappdev.models.auth.DataProvider
import ie.coconnor.mobileappdev.models.locations.LocationDetailsViewModel
import ie.coconnor.mobileappdev.models.locations.LocationsViewModel
import ie.coconnor.mobileappdev.models.plan.PlanViewModel
import ie.coconnor.mobileappdev.service.BootReceiver
import ie.coconnor.mobileappdev.service.LocationForegroundService
import ie.coconnor.mobileappdev.ui.locations.LocationDetailsScreen
import ie.coconnor.mobileappdev.ui.locations.LocationsScreen
import ie.coconnor.mobileappdev.ui.login.LoginScreen
import ie.coconnor.mobileappdev.ui.navigation.BottomBar
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.ui.plan.PlanScreen
import ie.coconnor.mobileappdev.ui.settings.SettingsScreen
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme
import ie.coconnor.mobileappdev.utils.SharedPref
import ie.coconnor.mobileappdev.utils.UIThemeController
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPref: SharedPref

    private val authViewModel by viewModels<AuthViewModel>()
    private val tourViewModel by viewModels<LocationsViewModel>()
    private val locationDetailsViewModel by viewModels<LocationDetailsViewModel> ()
    private val planViewModel by viewModels<PlanViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient

    private lateinit var textToSpeech: TextToSpeech


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        textToSpeech = TextToSpeech(this) {status ->
            if (status == TextToSpeech.SUCCESS){
                Timber.tag("TAG").d("TextToSpeech Initialization Success")

            }else{
                Timber.tag("TAG").d("TextToSpeech Initialization Failed")
            }
        }

        Timber.tag(TAG).i("Creating geofence client")
        geofencingClient = LocationServices.getGeofencingClient(this)
        //check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                 ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION
                ),
                REQUEST_CODE
            )
        }

        val receiver = ComponentName(this, BootReceiver::class.java)
        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }else{
            checkAndRequestLocationPermissions()
        }
        WindowCompat.setDecorFitsSystemWindows(window, true)
        createLocationRequest()

        setContent {
            val isDarkMode by UIThemeController.isDarkMode.collectAsState()
            MobileAppDevTheme (darkTheme = isDarkMode){
                val navController = rememberNavController()
                val currentUser = authViewModel.currentUser.collectAsState().value
                DataProvider.updateAuthState(currentUser)

                Timber.tag(TAG).i("Authenticated: ${DataProvider.isAuthenticated}")
                Timber.tag(TAG).i( "Anonymous: ${DataProvider.isAnonymous}")
                Timber.tag(TAG).i("User: ${DataProvider.user}")

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            modifier = Modifier
                        )
                    },
                    ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        NavigationGraph(navController = navController, authViewModel = authViewModel, tourViewModel = tourViewModel, locationDetailsViewModel = locationDetailsViewModel, planViewModel, sharedPref = sharedPref, geofencingClient)
                    }
                }
            }
        }
    }

    private fun currentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latlng = LatLng(location.latitude, location.longitude)
                Timber.tag(TAG).i("${latlng.toString()}")

            }
        }
    }


    private fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_INTERVAL
            fastestInterval = LOCATION_FASTEST_INTERVAL
            Priority.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Timber.tag(TAG).i("Location Enable Successful")

        }
        task.addOnFailureListener { exception ->
            Timber.tag(TAG).e("Location ${exception.message.toString()}")
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                    Timber.tag(TAG).e("$sendEx")
                }
            }
        }
    }

    private fun checkBackGroundLocationPermission(): Boolean {
        return  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            return true
        }

    }

    private fun requestBackGroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkAndRequestLocationPermissions() {
        if (checkLocationPermission()) {
            if (checkBackGroundLocationPermission()){
                Timber.tag(TAG).i("Start Location Service")
                startLocationService()
            }else{
                requestBackGroundLocationPermission()
            }

        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                checkAndRequestLocationPermissions()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                // permission denied permanently
                Timber.tag(TAG).i("Permission denied permanently")
            }
            else -> {
                requestNotificationPermission.launch(permission)
            }
        }
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted->
            if (isGranted)
                checkAndRequestLocationPermissions()
    }
    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        startService(serviceIntent)
    }
    fun stopLocationService() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        stopService(serviceIntent)
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}

@Composable
fun NavigationGraph(navController: NavHostController,
                    authViewModel: AuthViewModel,
                    tourViewModel: LocationsViewModel,
                    locationDetailsViewModel: LocationDetailsViewModel,
                    planViewModel: PlanViewModel,
                    sharedPref: SharedPref,
                    geoFencingClient: GeofencingClient) {
    var startDestination = Destinations.LocationsScreen.route

    if (DataProvider.authState == AuthState.SignedOut)
    {
        startDestination = Destinations.LoginScreen.route
    }
    NavHost(navController, startDestination = startDestination) {
        composable(Destinations.LoginScreen.route) {
            LoginScreen( authViewModel)
        }
        composable(Destinations.LocationsScreen.route) {
            LocationsScreen(tourViewModel, navController , sharedPref)
        }
        composable(Destinations.PlanScreen.route ) {
            PlanScreen(planViewModel, navController, geoFencingClient)
        }
        composable(Destinations.SettingsScreen.route) {
            SettingsScreen(navController, authViewModel)
        }
        composable(Destinations.LocationDetailsScreen.route + "/{location}", arguments = listOf(navArgument("location")  { type = NavType.StringType })
        ) { backStackEntry ->
            val location = backStackEntry.arguments?.getString("location")
            LocationDetailsScreen(locationDetailsViewModel, location)
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

