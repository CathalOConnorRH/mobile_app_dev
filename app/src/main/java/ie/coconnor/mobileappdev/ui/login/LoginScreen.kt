package ie.coconnor.mobileappdev.ui.login


import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ie.coconnor.mobileappdev.AuthViewModel
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.AuthState
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.ui.component.AnonymousSignIn
import ie.coconnor.mobileappdev.ui.component.GoogleSignIn
import ie.coconnor.mobileappdev.ui.component.OneTapSignIn
import ie.coconnor.mobileappdev.ui.theme.MobileAppDevTheme
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    loginState: MutableState<Boolean>? = null
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credentials = authViewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                authViewModel.signInWithGoogle(credentials)
            }
            catch (e: ApiException) {
                Log.e("LoginScreen:Launcher","Login One-tap $e")
            }
        }
        else if (result.resultCode == Activity.RESULT_CANCELED){
            Log.e("LoginScreen:Launcher","OneTapClient Canceled")
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    MobileAppDevTheme {
        Scaffold(
//            containerColor = MaterialTheme.colorScheme.primary
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
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f),
                    painter = painterResource(R.drawable.vector),
                    contentDescription = "app_logo",
                    contentScale = ContentScale.Fit,
//                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary)
                )

                if (DataProvider.authState == AuthState.SignedOut) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValues),
                        verticalAlignment = Alignment.Bottom,

                        ) {
                        var text by rememberSaveable { mutableStateOf("") }

                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = text,
                            onValueChange = { text = it },
                            label = { Text("Username") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = text,
                            onValueChange = { text = it },
                            label = { Text("Password")},
                            visualTransformation = PasswordVisualTransformation(),
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        ElevatedButton(
                            onClick = {
                                /* Do something! */
                            }
                        ) {
                            Text(text = "Create Account")
                        }
                    }
                    Button(
                        onClick = {
                            authViewModel.oneTapSignIn()
                        },
                        modifier = Modifier
                            .size(width = 300.dp, height = 50.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White
//                    )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = ""
                        )
                        Text(
                            text = "Sign in with Google",
                            modifier = Modifier.padding(6.dp),
//                        color = Color.Black.copy(alpha = 0.5f)
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
//                            color = MaterialTheme.colorScheme.primary
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
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White
//                    )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = ""
                        )
                        Text(
                            text = "Sign Out",
                            modifier = Modifier.padding(6.dp),
//                        color = Color.Black.copy(alpha = 0.5f)
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
        // Dismiss LoginScreen
        loginState?.let {
            it.value = false
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
   // MobileAppDevTheme {
//    LoginScreen(LoginScreen)
    //}
}