package ie.coconnor.mobileappdev.models.tour

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.models.LocationResponse
import ie.coconnor.mobileappdev.repository.TourRepository
import kotlinx.coroutines.launch


class TourViewModel : ViewModel() {
    private val repository = TourRepository()

    private val _tours = MutableLiveData<LocationResponse>()
    val tours: LiveData<LocationResponse> = _tours



    val apiKey: String = ""
    val location: String = "Waterford, Ireland"
    val category: String = "attractions"

    fun fetchTours() {
        viewModelScope.launch {
            try {
                val te = null
                val cards = repository.getLocations(apiKey, location, category)
                println(cards.data.toString())
                _tours.value = cards
                Log.e("TourViewModel", _tours.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("TourViewModel", e.message.toString());
            }
        }
    }
}
