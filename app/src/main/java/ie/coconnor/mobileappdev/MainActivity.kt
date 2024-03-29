package ie.coconnor.mobileappdev

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.datatransport.runtime.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.ui.login.LoginScreen
import ie.coconnor.mobileappdev.ui.login.SignUpScreen
import ie.coconnor.mobileappdev.ui.navigation.BottomBar
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.ui.screens.TestScreen
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme
//import com.google.firebase.firestore.FirebaseFirestore
import ie.coconnor.mobileappdev.models.AuthState
import ie.coconnor.mobileappdev.models.Constants
import ie.coconnor.mobileappdev.models.Constants.tripAdvisorApiKey
import ie.coconnor.mobileappdev.models.tour.TourViewModel
import ie.coconnor.mobileappdev.ui.screens.Attraction
import ie.coconnor.mobileappdev.ui.screens.SettingsScreen
import ie.coconnor.mobileappdev.ui.screens.TourScreen
import ie.coconnor.mobileappdev.utils.SharedPref
import ie.coconnor.mobileappdev.utils.UIThemeController
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPref: SharedPref
//    private var pressBackExitJob: Job? = null
//    private var backPressedOnce = false

    val authViewModel by viewModels<AuthViewModel>()
    val tourViewModel by viewModels<TourViewModel>()

//    cval db = FirebaseFirestore.getInstance()
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//
//        )
        window.setTitle("Test")
        UIThemeController.updateUITheme(sharedPref.getDarkMode())

        setContent {
            val isDarkMode by UIThemeController.isDarkMode.collectAsState()

            MobileAppDevTheme (darkTheme = isDarkMode){
                    val navController = rememberNavController()
                    var buttonsVisible = remember { mutableStateOf(true) }

    //                    NavHost(navController = navController, startDestination = "LoginScreen") {
    //                        composable("LoginScreen") { LoginScreen() }
    //                        composable("SignUpScreen") { SignUpScreen(navController) }
    //                        composable(
    //                            route = "screen2/{name}",
    //                            arguments = listOf(navArgument("name") { type = NavType.StringType })
    //                        ) { backStackEntry ->
    //                            Screen2(
    //                                navController,
    //                                name = backStackEntry.arguments?.getString("name") ?: ""
    //                            )
    //                        }
    //                    }
                    val currentUser = authViewModel.currentUser.collectAsState().value
                    DataProvider.updateAuthState(currentUser)

                    Log.i("AuthRepo", "Authenticated: ${DataProvider.isAuthenticated}")
                    Log.i("AuthRepo", "Anonymous: ${DataProvider.isAnonymous}")
                    Log.i("AuthRepo", "User: ${DataProvider.user}")

    //                    if (DataProvider.authState != AuthState.SignedOut) {
    //                        ArticlesScreen(authViewModel)
    //                    } else {
    //                        LoginScreen(authViewModel)
    //                    }
    //                    LoginScreen()

                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                navController = navController,
                                state = buttonsVisible,
                                modifier = Modifier
                            )
                        }) { paddingValues ->
                        Box(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            NavigationGraph(navController = navController, authViewModel = authViewModel, tourViewModel = tourViewModel)
                        }
                    }
                }

            }
        }
}

@Composable
fun NavigationGraph(navController: NavHostController, authViewModel: AuthViewModel, tourViewModel: TourViewModel) {
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
        composable(Destinations.TourScreen.route) {
            TourScreen(tourViewModel)
        }
        composable(Destinations.Notification.route) {
            //ArticlesScreen(navController)
        }
        composable(Destinations.TestScreen.route) {
            TestScreen(navController)
        }
        composable(Destinations.SettingsScreen.route) {
//            SettingsScreen(navController, authViewModel)
            SettingsScreen(navController, authViewModel)

        }
    }
}

@Composable
fun KeyboardAware(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.imePadding()) {
        content()
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
