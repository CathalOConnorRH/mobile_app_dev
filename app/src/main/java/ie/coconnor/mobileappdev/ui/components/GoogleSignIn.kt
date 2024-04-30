package ie.coconnor.mobileappdev.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ie.coconnor.mobileappdev.models.auth.DataProvider
import ie.coconnor.mobileappdev.models.Response

@Composable
fun GoogleSignIn(
    launch: () -> Unit
) {
    when (val signInWithGoogleResponse = DataProvider.googleSignInResponse) {
        is Response.Loading -> {
            Log.i("Login:GoogleSignIn", "Loading")
            AuthLoginProgressIndicator()
        }
        is Response.Success -> signInWithGoogleResponse.data?.let { authResult ->
            Log.i("Login:GoogleSignIn", "Success: $authResult")
            launch()
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.e("Login:GoogleSignIn", "${signInWithGoogleResponse.e}")
        }
    }
}