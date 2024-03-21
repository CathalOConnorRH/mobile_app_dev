package ie.coconnor.mobileappdev

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ie.coconnor.mobileappdev.models.AuthState
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.screens.ArticlesScreen
import ie.coconnor.mobileappdev.ui.login.LoginScreen
import ie.coconnor.mobileappdev.ui.login.SignUpScreen
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileAppDevTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "LoginScreen") {
                        composable("LoginScreen") { LoginScreen() }
                        composable("SignUpScreen") { SignUpScreen(navController = navController)}
//                        composable(
//                            route = "screen2/{name}",
//                            arguments = listOf(navArgument("name") { type = NavType.StringType })
//                        ) { backStackEntry ->
//                            Screen2(
//                                navController,
//                                name = backStackEntry.arguments?.getString("name") ?: ""
//                            )
//                        }
                    }
                    val currentUser = authViewModel.currentUser.collectAsState().value
                    DataProvider.updateAuthState(currentUser)

                    Log.i("AuthRepo", "Authenticated: ${DataProvider.isAuthenticated}")
                    Log.i("AuthRepo", "Anonymous: ${DataProvider.isAnonymous}")
                    Log.i("AuthRepo", "User: ${DataProvider.user}")

                    if (DataProvider.authState != AuthState.SignedOut) {
                        ArticlesScreen(authViewModel)
                    } else {
                        LoginScreen(authViewModel)
                    }
//                    LoginScreen()
                }
            }
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