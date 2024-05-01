package ie.coconnor.mobileappdev.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ie.coconnor.mobileappdev.models.Response
import ie.coconnor.mobileappdev.models.auth.DataProvider
import timber.log.Timber

@Composable
fun AnonymousSignIn() {
    val TAG = "AnonymousSignIn"
    when (val anonymousResponse = DataProvider.anonymousSignInResponse) {
        is Response.Loading -> {
            Timber.tag(TAG).i("Login:AnonymousSignIn Loading")
            AuthLoginProgressIndicator()
        }
        is Response.Success -> anonymousResponse.data?.let { authResult ->
            Timber.tag(TAG).i("Login:AnonymousSignIn Success: $authResult")
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Timber.tag(TAG).e("Login:AnonymousSignIn ${anonymousResponse.e}")
        }

    }

}
