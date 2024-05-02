package ie.coconnor.mobileappdev.repository

import com.google.firebase.firestore.toObjects
import ie.coconnor.mobileappdev.models.Constants
import ie.coconnor.mobileappdev.models.auth.DataProvider
import ie.coconnor.mobileappdev.models.locations.Location
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirestoreRepository {
    private val collectionName = "trips"
    suspend fun getTrips(): List<Trip> {
        val querySnapshot = Constants.db.collection(collectionName)
            .whereEqualTo("username", DataProvider.user?.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Timber.tag(TAG).i("Trips received => ${document.id}")
                }
            }
            .addOnFailureListener { e -> Timber.tag(TAG).e("Error getting trips $e") }
            .await()

        return querySnapshot.toObjects<Trip>()
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
            .addOnSuccessListener { Timber.tag(TAG).i("Document $selectedLocation successfully deleted!") }
            .addOnFailureListener { e ->Timber.tag(TAG).e("Error deleting document $e") }.await()
    }

    companion object {
        private const val TAG = "FirestoreRepository"
    }
}

data class Trip(
    var location: Location? = null,
    var username: String? = "",
)
