package ie.coconnor.mobileappdev.models.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.repository.FirestoreRepository
import ie.coconnor.mobileappdev.repository.LocationsRepository
import ie.coconnor.mobileappdev.repository.Trip
import kotlinx.coroutines.launch
import timber.log.Timber

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
                Timber.tag(TAG).i(_locations.value.toString())
            } catch (e: Exception) {
                // Handle error
                Timber.tag(TAG).e( e.message.toString());
            }
        }
    }

    fun fetchTrips(){
        viewModelScope.launch {
            try {
                val trips = fireStoreRepository.getTrips()
                println(trips.toString())
                _trips.value = trips
                Timber.tag(TAG).i( _trips.value.toString())
            } catch (e: Exception) {
                // Handle error
                Timber.tag(TAG).e( e.message.toString());
            }
        }
    }

    fun createTrip(location: Location){
        viewModelScope.launch {
            try {
                Timber.tag(TAG).i( location.location_id)
                fireStoreRepository.createTrip(location)
            } catch (e: Exception){
                Timber.tag(TAG).e( e.message.toString());
            }
        }
    }
    companion object {
        private const val TAG = "LocationsViewModel"
    }

}
