package ie.coconnor.mobileappdev.models.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.repository.FirestoreRepository
import ie.coconnor.mobileappdev.repository.Trip
import kotlinx.coroutines.launch
import timber.log.Timber

class PlanViewModel: ViewModel()  {

    private val repository = FirestoreRepository()

    private val _trips = MutableLiveData<List<Trip>>()

    val trips: LiveData<List<Trip>> = _trips

    fun fetchTrips() {
        viewModelScope.launch {
            try {
                val cards = repository.getTrips()
                _trips.value = cards
                Timber.tag(TAG).i(_trips.value.toString())
            } catch (e: Exception) {
                Timber.tag(TAG).e( e.message.toString());
            }
        }
    }

    fun onSwipeToDelete(selectedLocation: String){
        viewModelScope.launch {
            try {
                repository.removeLocation(selectedLocation)
                Timber.tag(TAG).i("$selectedLocation removed from trips")
                val cards = repository.getTrips()
                _trips.value = cards
            } catch (e: Exception) {
                Timber.tag(TAG).e( e.message.toString());
            }
        }
    }
    companion object {
        private const val TAG = "PlanViewModel"
    }
}
