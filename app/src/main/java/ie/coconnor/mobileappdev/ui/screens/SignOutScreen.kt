package ie.coconnor.mobileappdev.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ie.coconnor.mobileappdev.CustomAppBar

@Composable
fun SignOutScreen() {
    Scaffold(
        topBar = {
            CustomAppBar(
                currentScreen = "TopAppBar",
                showBackButton = true,
                onBackButtonClick = {  }
            )
        }     ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = " Settings Screen")
        }
    }
}