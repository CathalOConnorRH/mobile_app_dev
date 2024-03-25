package ie.coconnor.mobileappdev.ui.screens


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.coconnor.mobileappdev.CustomAppBar
import ie.coconnor.mobileappdev.models.Constants.db
import ie.coconnor.mobileappdev.ui.navigation.Destinations

@Composable
fun TestScreen(navController: NavHostController) {
    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Test Screen")

            Button(
                onClick = {
                          readAttractions()
                },
                modifier = Modifier
                    .size(width = 200.dp, height = 50.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "Read Attraction",
                    modifier = Modifier.padding(6.dp),
//                            color = MaterialTheme.colorScheme.primary
                )
            }

            val attraction = Attraction("Reginald's Tower")

            Button(
                onClick = {
                    createAttractions(attraction)
                },
                modifier = Modifier
                    .size(width = 200.dp, height = 50.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "Create Attraction",
                    modifier = Modifier.padding(6.dp),
//                            color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = {
                    deleteAttraction(attraction)
                },
                modifier = Modifier
                    .size(width = 200.dp, height = 50.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "Delete Attraction",
                    modifier = Modifier.padding(6.dp),
//                            color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

fun getAttraction(attraction: Attraction) {
    var attractionName = attraction.name.filter { it.isLetterOrDigit() }

    db.collection("attractions")
        .document(attractionName)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val user = document.toObject(Attraction::class.java)
                // Use the user object
            } else {
                // Document doesn't exist
            }
        }
        .addOnFailureListener { e ->
            // Handle any errors
        }
}

fun deleteAttraction(attraction: Attraction) {
    var attractionName = attraction.name.filter { it.isLetterOrDigit() }
    db.collection("attractions")
        .document(attractionName)
        .delete()
        .addOnSuccessListener {
            // Document deleted successfully
            Log.d("TAG", "Attraction Delete => ${attraction.toString()}")

        }
        .addOnFailureListener { e ->
            // Handle any errors
            Log.d("TAG", "Attraction Not deleted => ${e.toString()}")

        }
}

fun createAttractions(attraction: Attraction) {
    var attractionName = attraction.name.filter { it.isLetterOrDigit() }
    db.collection("attractions")
        .document(attractionName)
        .set(attraction)
        .addOnSuccessListener {
            // Document created successfully
            Log.d("TAG", "Attraction Created => ${attraction.toString()}")

        }
        .addOnFailureListener { e ->
            // Handle any errors
        }

}

fun readAttractions() {
    db.collection("attractions")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                Log.d("TAG", "${document.id} => ${document.data}")
            }
        }
        .addOnFailureListener { exception ->
            Log.d("TAG", "Error getting documents: ", exception)
        }
}

data class Attraction(
    val name: String = ""
)
