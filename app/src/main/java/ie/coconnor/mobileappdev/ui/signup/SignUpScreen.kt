package ie.coconnor.mobileappdev.ui.login

import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ElevatedButton

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ie.coconnor.mobileappdev.CustomAppBar
import ie.coconnor.mobileappdev.R

@Composable
fun SignUpScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CustomAppBar(
                currentScreen = "TopAppBar",
                showBackButton = true,
                onBackButtonClick = { navController.popBackStack() }
            )
        }     ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally

            //verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.TopCenter,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.red_hat_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
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
        }

    }
}

@Preview
@Composable
fun SignUpScreenPreview(){
    val navController = rememberNavController()
    SignUpScreen(navController)
}