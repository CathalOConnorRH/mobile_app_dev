package ie.coconnor.mobileappdev.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.auth.api.identity.BeginSignInResult
import ie.coconnor.mobileappdev.models.Response
import ie.coconnor.mobileappdev.models.auth.DataProvider
import timber.log.Timber

@Composable
fun OneTapSignIn(
    launch: (result: BeginSignInResult) -> Unit
) {
    val TAG = "OneTapSignIn"
    when(val oneTapSignInResponse = DataProvider.oneTapSignInResponse) {
        is Response.Loading ->  {
            Timber.tag(TAG).i("Login:OneTap Loading")
            AuthLoginProgressIndicator()
        }
        is Response.Success -> oneTapSignInResponse.data?.let { signInResult ->
            LaunchedEffect(signInResult) {
                launch(signInResult)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Timber.tag(TAG).e("Login:OneTap ${oneTapSignInResponse.e}")
        }
    }
}