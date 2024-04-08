package ie.coconnor.mobileappdev.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.coconnor.mobileappdev.AuthViewModel
import ie.coconnor.mobileappdev.CustomAppBar
import ie.coconnor.mobileappdev.models.Constants.db
import ie.coconnor.mobileappdev.models.tour.Tour
import ie.coconnor.mobileappdev.ui.screens.CardList

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