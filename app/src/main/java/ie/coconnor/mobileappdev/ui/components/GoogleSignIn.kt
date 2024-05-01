package ie.coconnor.mobileappdev.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ie.coconnor.mobileappdev.models.Response
import ie.coconnor.mobileappdev.models.auth.DataProvider
import timber.log.Timber

@Composable
fun GoogleSignIn(
    launch: () -> Unit
) {
    val TAG = "GoogleSignIn"

    when (val signInWithGoogleResponse = DataProvider.googleSignInResponse) {
        is Response.Loading -> {
            Timber.tag(TAG).i("Login:GoogleSignIn Loading")
            AuthLoginProgressIndicator()
        }
        is Response.Success -> signInWithGoogleResponse.data?.let { authResult ->
            Timber.tag(TAG).i("Login:GoogleSignIn Success: $authResult")
            launch()
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Timber.tag(TAG).e("Login:GoogleSignIn ${signInWithGoogleResponse.e}")
        }
    }
}