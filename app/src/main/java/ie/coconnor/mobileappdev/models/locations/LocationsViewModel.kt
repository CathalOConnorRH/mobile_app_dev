package ie.coconnor.mobileappdev.models.locations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.models.Location
import ie.coconnor.mobileappdev.models.LocationResponse
import ie.coconnor.mobileappdev.repository.FirestoreRepository
import ie.coconnor.mobileappdev.repository.LocationsRepository
import ie.coconnor.mobileappdev.repository.Trip
import kotlinx.coroutines.launch

class LocationsViewModel() : ViewModel() {

    private val repository = LocationsRepository()
    private val fireStoreRepository = FirestoreRepository()

    private val _locations = MutableLiveData<LocationResponse>()
    val locations: LiveData<LocationResponse> = _locations

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips

    private val category: String = "attractions"

    fun fetchTours(location: String, tripAdvisorApiKey: String) {
        viewModelScope.launch {
            try {
                val cards = repository.getLocations(tripAdvisorApiKey, location, category)
                println(cards.data.toString())
                _locations.value = cards
                Log.e("TourViewModel", _locations.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("TourViewModel", e.message.toString());
            }
        }
    }

    fun fetchTrips(){
        viewModelScope.launch {
            try {
                val cards = fireStoreRepository.getTrips()
                println(cards.toString())
                _trips.value = cards
                Log.e("TourViewModel", _trips.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("TourViewModel", e.message.toString());
            }
        }
    }
    fun createOrUpdateTrip(location: Location, documentName: String){
        viewModelScope.launch {
            try {
                Log.i("PlanViewModel", location.toString())
                val cards = fireStoreRepository.createTrip(location, documentName)
                println(cards.toString())
            } catch (e: Exception){
                Log.e("PlanViewModel", e.message.toString());
            }
        }
    }

}

//    fun getSavedLocations(){
//        viewModelScope.launch {
//        try {
//            val querySnapshot = Constants.db.collection("trips")
//                .whereEqualTo("user", "A0jtgW8DU8cmYzDhVXJjg8sv4Iu1")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        Log.d("TAG", "Attraction Created => ${document.toString()}")
//                    }
//                }
//                .addOnFailureListener { e ->
//                    // Handle any errors
//                }
//                .await()
//
//            _savedLocations.value = querySnapshot.toObjects<listOfTrips>()
//        } catch (e: Exception) {
//            // Handle error
//            Log.e("TourViewModel", e.message.toString());
//        }
//
//    }
//    fun saveLocation(location: Location) {
//
//        var trips: MutableList<Trip> = mutableListOf()
//
//        viewModelScope.launch {
//            try {
//                println("Save location ${location.name}")
//
//                val querySnapshot = Constants.db.collection("trips")
//                    .whereEqualTo("user", "A0jtgW8DU8cmYzDhVXJjg8sv4Iu1")
//                    .get()
//                    .addOnSuccessListener { result ->
////                val trips = result.toObjects(Trip::class.java)
//                        for (document in result) {
////                            val trip = document.toObject<listOfTrips>()
//////                    var trip: Trip = document.get("user").toString()
////                            // Document created successfully
//                            Log.d("TAG", "Attraction Created => ${document.toString()}")
////                            trips.add(trip)
////                    trip.add(document)
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        // Handle any errors
//                    }
//                    .await()
//
//                //Check if a trip exists...
//                // if it does, ask which one to save to
//                // if not ask to create and then save
//
////        var trips: MutableList<Trip> = mutableListOf()
//
////        trip.pass = "test"
//
////                val trips = Constants.db.collection("trips")
////                    .whereEqualTo("user", "A0jtgW8DU8cmYzDhVXJjg8sv4Iu1")
////
////                trips.get()
////                    .addOnSuccessListener { result ->
//////                val trips = result.toObjects(Trip::class.java)
////                        for (document in result) {
////                            val trip = document.toObject<listOfTrips>()
//////                    var trip: Trip = document.get("user").toString()
////                            // Document created successfully
////                            Log.d("TAG", "Attraction Created => ${document.toString()}")
//////                    trips.add(Trip(document.data["testField"].toString(), document.data["user"].toString()))
//////                    trip.add(document)
////                        }
////                    }
////                    .addOnFailureListener { e ->
////                        // Handle any errors
////                    }
////
////
//
//                println(querySnapshot.documents.size)
//
//            } catch (e: Exception) {
//                // Handle error
//                Log.e("TourViewModel", e.message.toString());
//            }
////        val trips = Constants.db.collection("trip").document("test")
////            trips.get()
////            .addOnSuccessListener { result ->
////                if(result != null ){
////                    Log.d("TAG", "${result.id} => ${result.data}")
////
////                }
//////                for (document in result) {
////
//////                }
////            }
////            .addOnFailureListener { exception ->
////                Log.d("TAG", "Error getting documents: ", exception)
////            }
////
////        println("Trips ${trips.}")
//
//        }
//    }
//}
//
//data class listOfTrips(
//    val trips: List<Trip>
//)
//data class Trip(
//    var pass:String,
//    var userName:String
//)