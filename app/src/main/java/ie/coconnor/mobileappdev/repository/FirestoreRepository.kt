package ie.coconnor.mobileappdev.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.toObjects
import ie.coconnor.mobileappdev.models.Constants
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.models.Location
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    suspend fun getTrips(): List<Trip> {
        val querySnapshot = Constants.db.collection("trips")
                .whereEqualTo("username", DataProvider.user?.uid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("TAG", "Trips recevied => ${document.toString()}")
                    }
                }
                .addOnFailureListener { e ->
                    // Handle any errors
                }
                .await()

        var trips: List<Trip> = querySnapshot.toObjects<Trip>()

        return trips
    }

    suspend fun updateTrip(tripName: Location, documentName: String): String{

        var trips = Constants.db.collection("trips").document(documentName)
            .update("locations", FieldValue.arrayUnion(tripName))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }.await()
        return trips.toString()
        return ""
    }
    suspend fun createTrip(tripName: Location, documentName: String): String{
        val trip = Trip(
            tripName.name,
             DataProvider.user?.uid,
            listOf(tripName)
        )

        var trips = Constants.db.collection("trips").document(documentName)
            .set(tripName)
            .addOnSuccessListener { Log.d(TAG, "Document ${documentName} successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document ${documentName}", e) }.await()
        return trips.toString()
    }
    companion object {
        private const val TAG = "FirestoreRepository"
    }
}

data class Trip(
    var name: String? = "",
    var username: String? = "",
    var locations: List<Location>? = null,
)
