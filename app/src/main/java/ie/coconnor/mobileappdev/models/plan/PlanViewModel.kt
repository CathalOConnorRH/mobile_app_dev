package ie.coconnor.mobileappdev.models.plan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.models.Location
import ie.coconnor.mobileappdev.models.locations.LocationDetailsViewModel
import ie.coconnor.mobileappdev.repository.FirestoreRepository
import ie.coconnor.mobileappdev.repository.Trip
import kotlinx.coroutines.launch
import timber.log.Timber

class PlanViewModel: ViewModel()  {

    private val repository = FirestoreRepository()

    private val _trips = MutableLiveData<List<Trip>>()

    val trips: LiveData<List<Trip>> = _trips

    fun createTrip(tripName: Location, documentName: String){
        viewModelScope.launch {
            try {
                Timber.tag(TAG).i( tripName.toString())
                val cards = repository.createTrip(tripName, documentName)
            } catch (e: Exception){
                Timber.tag(TAG).i( e.message.toString());
            }
        }
    }

    fun fetchTrips() {
        viewModelScope.launch {
            try {
                val cards = repository.getTrips()
                _trips.value = cards
                Timber.tag(TAG).i(_trips.value.toString())
            } catch (e: Exception) {
                // Handle error
                Timber.tag(TAG).e( e.message.toString());
            }
        }
    }

    companion object {
        private const val TAG = "PlanViewModel"
    }
}