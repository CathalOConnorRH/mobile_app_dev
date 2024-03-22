package ie.coconnor.mobileappdev.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import ie.coconnor.mobileappdev.CustomAppBar
import ie.coconnor.mobileappdev.KeyboardAware
import ie.coconnor.mobileappdev.R

@Composable
fun LoginScreenOld(navController: NavHostController) {
    var auth = Firebase.auth

    KeyboardAware {
        Scaffold(
            topBar = {
                CustomAppBar(
                    currentScreen = "TopAppBar",
                    showBackButton = true,
                    onBackButtonClick = {  }
                )
            }         ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally

                //verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.TopCenter,
                ) {
                    val painter = painterResource(id = R.drawable.red_hat_logo)

                    Image(
                        painter = painter,
                        contentDescription = "Logo",
                        contentScale = ContentScale.Fit
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    verticalAlignment = Alignment.Bottom,

                    ) {
                    var email by rememberSaveable { mutableStateOf("") }
                    var password by rememberSaveable { mutableStateOf("") }
                    var showPassword by remember { mutableStateOf(value = false) }

                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        value = email,
                        onValueChange = { email = it },
                        shape = RoundedCornerShape(percent = 20),
                        label = { Text("Username") },

                        )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        shape = RoundedCornerShape(percent = 20),
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            if (showPassword) {
                                IconButton(onClick = { showPassword = false }) {
                                    Icon(
                                        imageVector = Icons.Filled.Visibility,
                                        contentDescription = "hide_password"
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { showPassword = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.VisibilityOff,
                                        contentDescription = "hide_password"
                                    )
                                }
                            }
                        }
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
                        Text(text = "Sign In")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    ElevatedButton(
                        onClick = {
                            /* Do something! */
                            navController.navigate("SignUpScreen")
                        }
                    ) {
                        Text(text = "Create Account")
                    }
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    ElevatedButton(
                        onClick = {

                        }
                    ) {
                        Text(text = "Google Sign In")
                    }
                }
            }

        }
    }
}
@Preview
@Composable
fun LoginScreenPreview(){
    val navController = rememberNavController()
    LoginScreenOld(navController)
}