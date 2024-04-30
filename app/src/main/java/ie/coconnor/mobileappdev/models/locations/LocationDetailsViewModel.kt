package ie.coconnor.mobileappdev.models.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.repository.LocationsRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class LocationDetailsViewModel(): ViewModel() {
    private val repository = LocationsRepository()

    private val _locationDetails = MutableLiveData<LocationDetails>()
    val locationDetails: LiveData<LocationDetails> = _locationDetails

    fun fetchLocationDetails(location_id: String, tripAdvisorApiKey: String) {
        viewModelScope.launch {
            try {
                val details = repository.getLocationDetails(location_id, tripAdvisorApiKey)
                _locationDetails.value = details
                Timber.tag(TAG).i( _locationDetails.value.toString())
            } catch (http: HttpException) {
                Timber.tag(TAG).e( http.response().toString())
            } catch (e: Exception) {
                // Handle error
                Timber.tag(TAG).e( e.cause.toString());
                Timber.tag(TAG).e( e.message.toString());
                Timber.tag(TAG).e( e.stackTrace.contentToString());
            }
        }
    }

    companion object {
        private const val TAG = "LocationDetailsViewModel"
    }
}
