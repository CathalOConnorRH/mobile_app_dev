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
import ie.coconnor.mobileappdev.models.Constants.db

private val cardsRef = db.collection("attractions")
@Composable
fun ArticlesScreen(

){
    Scaffold(
//    topBar = {
//        CustomAppBar(
//            currentScreen = "TopAppBar",
//            showBackButton = true,
//            onBackButtonClick = { }
//        )
//    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "About Screen")
        }

    }
}