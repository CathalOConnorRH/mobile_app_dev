package ie.coconnor.mobileappdev.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.coconnor.mobileappdev.AuthViewModel
import ie.coconnor.mobileappdev.CustomAppBar
import ie.coconnor.mobileappdev.R

@Composable
fun SettingsScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    Scaffold(
         ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = " Settings Screen")
        }
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