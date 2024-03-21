package ie.coconnor.mobileappdev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ie.coconnor.mobileappdev.ui.login.LoginScreen
import ie.coconnor.mobileappdev.ui.login.SignUpScreen
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme

class MainActivity : ComponentActivity() {

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
                        composable("LoginScreen") { LoginScreen(navController = navController) }
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