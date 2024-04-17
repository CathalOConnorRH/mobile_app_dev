package ie.coconnor.mobileappdev.repository

import android.util.Log
import com.google.firebase.firestore.toObjects
import ie.coconnor.mobileappdev.models.Constants
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.models.Location
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    suspend fun getTrips(): List<Trip> {
        val querySnapshot = Constants.db.collection("trips")
                .whereEqualTo("user", DataProvider.user?.uid)
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

    suspend fun createOrUpdateTrip(tripName: Location): String{
        val trip = hashMapOf(
            "name" to tripName.name,
             "user" to (DataProvider.user?.uid ?: ""),
             "location" to {tripName}
        )

        var trips = Constants.db.collection("trips").document("iWywtwQ04MXkETRcaqDD")
            .update(trip)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }.await()
        println("HERE ${trip.get("name")}")
        return trip.toString()
    }
    companion object {
        private const val TAG = "FirestoreRepository"
    }
}
data class Trips(
    var data: List<Trip>
)
data class Trip(
    var name: String? = "",
    var username: String? = "",
    var locations: List<Location>? = null,
)
