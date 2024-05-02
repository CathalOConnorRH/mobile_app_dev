package ie.coconnor.mobileappdev.ui.login


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.auth.AuthState
import ie.coconnor.mobileappdev.models.auth.AuthViewModel
import ie.coconnor.mobileappdev.models.auth.DataProvider
import ie.coconnor.mobileappdev.ui.components.AnonymousSignIn
import ie.coconnor.mobileappdev.ui.components.GoogleSignIn
import ie.coconnor.mobileappdev.ui.components.OneTapSignIn
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme
import timber.log.Timber

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    loginState: MutableState<Boolean>? = null
) {
    val TAG = "LoginScreen"

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credentials = authViewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                authViewModel.signInWithGoogle(credentials)
            }
            catch (e: ApiException) {
                Timber.tag(TAG).e("LoginScreen:Launcher Login One-tap $e")
            }
        }
        else if (result.resultCode == Activity.RESULT_CANCELED){
            Timber.tag(TAG).e("LoginScreen:Launcher OneTapClient Canceled")
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    MobileAppDevTheme {
        Scaffold(
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopCenter),
                Arrangement.spacedBy(8.dp),
                Alignment.CenterHorizontally
            ) {
                val drawableResource = R.drawable.vector
                Image(
                    painter = painterResource(id = drawableResource),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f)
                )
                if (DataProvider.authState == AuthState.SignedOut || DataProvider.isAnonymous) {
                    Button(
                        onClick = {
                            authViewModel.oneTapSignIn()
                        },
                        modifier = Modifier
                            .size(width = 300.dp, height = 50.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = ""
                        )
                        Text(
                            text = "Sign in with Google",
                            modifier = Modifier.padding(6.dp),
                        )
                    }

                    Button(
                        onClick = {
                            authViewModel.signInAnonymously()
                        },
                        modifier = Modifier
                            .size(width = 200.dp, height = 50.dp)
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = "Skip",
                            modifier = Modifier.padding(6.dp),
                        )
                    }
                }else{
                    Button(
                        onClick = {
                            authViewModel.signOut()
                        },
                        modifier = Modifier
                            .size(width = 300.dp, height = 50.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = ""
                        )
                        Text(
                            text = "Sign Out",
                            modifier = Modifier.padding(6.dp),
                        )
                    }
                }
            }
        }
    }

    AnonymousSignIn()

    OneTapSignIn (
        launch = {
            launch(it)
        }
    )

    GoogleSignIn {
        loginState?.let {
            it.value = false
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val loginState: MutableState<Boolean>? = null
    MobileAppDevTheme {
    LoginScreen(authViewModel, loginState)
    }
}