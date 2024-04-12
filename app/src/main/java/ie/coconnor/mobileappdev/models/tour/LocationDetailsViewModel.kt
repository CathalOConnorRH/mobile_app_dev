package ie.coconnor.mobileappdev.models.tour

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.coconnor.mobileappdev.models.Constants.tripAdvisorApiKey
import ie.coconnor.mobileappdev.models.Location
import ie.coconnor.mobileappdev.models.LocationDetails
import ie.coconnor.mobileappdev.repository.LocationsRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class LocationDetailsViewModel(): ViewModel() {
    private val repository = LocationsRepository()

    private val _locationDetails = MutableLiveData<LocationDetails>()
    val locationDetails: LiveData<LocationDetails> = _locationDetails

//    val location: String = "Waterford, Ireland"
//    val category: String = "attractions"

    fun fetchLocationDetails(location_id: String) {
        viewModelScope.launch {
            try {
//                println("View Model shared Location ID " + location?.location_id.toString())
                val cards = repository.getLocationDetails(location_id ?: "", tripAdvisorApiKey)
                println("location " + cards.description.toString())
                _locationDetails.value = cards
                Log.e("TourViewModel", _locationDetails.value.toString())
            } catch (http: HttpException) {
                Log.e("TourViewModel", http.response().toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("TourViewModel", e.cause.toString());
                Log.e("TourViewModel", e.message.toString());
                Log.e("TourViewModel", e.stackTrace.contentToString());


            }
        }
    }
}