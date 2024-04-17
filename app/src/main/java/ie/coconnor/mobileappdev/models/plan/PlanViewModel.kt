package ie.coconnor.mobileappdev.models.plan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.models.Location
import ie.coconnor.mobileappdev.repository.FirestoreRepository
import ie.coconnor.mobileappdev.repository.Trip
import kotlinx.coroutines.launch

class PlanViewModel: ViewModel()  {

    private val repository = FirestoreRepository()

    private val _trips = MutableLiveData<List<Trip>>()

    val trips: LiveData<List<Trip>> = _trips

    fun createTrip(tripName: Location, documentName: String){
        viewModelScope.launch {
            try {
                Log.i("PlanViewModel", tripName.toString())
                val cards = repository.createTrip(tripName, documentName)
                println(cards.toString())
            } catch (e: Exception){
                Log.e("PlanViewModel", e.message.toString());
            }
        }
    }

    fun fetchTrips() {
        viewModelScope.launch {
            try {
                val cards = repository.getTrips()
                println(cards.toString())
                _trips.value = cards
                Log.e("PlanViewModel", _trips.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("PlanViewModel", e.message.toString());
            }
        }
    }
}
