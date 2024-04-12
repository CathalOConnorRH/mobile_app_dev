package ie.coconnor.mobileappdev.models.tour

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.coconnor.mobileappdev.models.Constants.tripAdvisorApiKey
import ie.coconnor.mobileappdev.models.Location
import ie.coconnor.mobileappdev.models.LocationResponse
import ie.coconnor.mobileappdev.repository.LocationsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsViewModel() : ViewModel() {

    private val repository = LocationsRepository()

    private val _locations = MutableLiveData<LocationResponse>()
    val locations: LiveData<LocationResponse> = _locations

    val location: String = "Waterford, Ireland"
    val category: String = "attractions"

    fun fetchTours() {
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
}