package ie.coconnor.mobileappdev

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.ui.login.LoginScreen
import ie.coconnor.mobileappdev.ui.login.SignUpScreen
import ie.coconnor.mobileappdev.ui.navigation.BottomBar
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val authViewModel by viewModels<AuthViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MobileAppDevTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
//                ) {
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
                            NavigationGraph(navController = navController, authViewModel = authViewModel)
                        }
                    }
                }

            }
        }
    }
//}

@Composable
fun NavigationGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController, startDestination = Destinations.LoginScreen.route) {
        composable(Destinations.LoginScreen.route) {
            LoginScreen( authViewModel)
        }
        composable(Destinations.Favourite.route) {
            SignUpScreen(navController)
        }
        composable(Destinations.Notification.route) {
            //ArticlesScreen(navController)
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
