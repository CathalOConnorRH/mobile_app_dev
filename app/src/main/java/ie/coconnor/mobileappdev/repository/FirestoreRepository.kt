package ie.coconnor.mobileappdev.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.toObjects
import ie.coconnor.mobileappdev.models.Constants
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.models.Location
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirestoreRepository {
    val collectionName = "trips"
    suspend fun getTrips(): List<Trip> {
        println(DataProvider.user?.uid)
        val querySnapshot = Constants.db.collection(collectionName)
            .whereEqualTo("username", DataProvider.user?.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Timber.tag(TAG).i("Trips recevied => ${document.toString()}")
                }
            }
            .addOnFailureListener { e ->
                // Handle any errors
            }
            .await()

        return querySnapshot.toObjects<Trip>()
    }

    suspend fun updateTrip(location: Location, documentName: String): String{

//        val trip = Trip(
//            documentName,
//            DataProvider.user?.uid
//        )

        var trips = Constants.db.collection(collectionName).document(documentName)
            .update("locations", FieldValue.arrayUnion(location))
            .addOnSuccessListener { Timber.tag(TAG).d("DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Timber.tag(TAG).e( "Error writing document $e") }.await()
        return trips.toString()
        return ""
    }
    suspend fun createTrip(location: Location){
        val trip = Trip(
            location,
             DataProvider.user?.uid
        )

        Constants.db.collection(collectionName).document(location.location_id.toString() + "::" + DataProvider.user?.uid)
            .set(trip)
            .addOnSuccessListener { Timber.tag(TAG).d( "Document ${location.location_id.toString() + "::" + DataProvider.user?.uid} successfully written!") }
            .addOnFailureListener { e -> Timber.tag(TAG).e("Error writing document ${location.location_id.toString() + "::" + DataProvider.user?.uid} $e") }.await()
    }

    suspend fun removeLocation(selectedLocation: String) {
        Constants.db.collection(collectionName).document(selectedLocation + "::" + DataProvider.user?.uid)
            .delete()
            .addOnSuccessListener { Timber.tag(TAG).i("Document ${selectedLocation} successfully deleted!") }
            .addOnFailureListener { e ->Timber.tag(TAG).e("Error deleting document ${e}") }.await()

    }

    companion object {
        private const val TAG = "FirestoreRepository"
    }
}

data class Trip(
    var location: Location? = null,
    var username: String? = "",
)
