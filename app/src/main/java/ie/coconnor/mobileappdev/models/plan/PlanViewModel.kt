package ie.coconnor.mobileappdev.models.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import ie.coconnor.mobileappdev.models.Location
import ie.coconnor.mobileappdev.repository.FirestoreRepository
import ie.coconnor.mobileappdev.repository.Trip
import kotlinx.coroutines.launch
import timber.log.Timber

class PlanViewModel: ViewModel()  {

    private val repository = FirestoreRepository()

    private val _trips = MutableLiveData<List<Trip>>()

    val trips: LiveData<List<Trip>> = _trips

    val geofencingClient = LocationServices.getGeofencingClient(this@MainActivity)
    fun createTrip( location: Location){
        viewModelScope.launch {
            try {
                Timber.tag(TAG).i( location.location_id)
                repository.createTrip(location)
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

    fun addGeofenceRequest() {
        geofencingClient.addGeofences(getGeofenceRequest(), geofencePendingIntent).run {
            addOnSuccessListener {
                Toast.makeText(
                    this@MainActivity,
                    "Geofence is added successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            addOnFailureListener {
                Log.e("Error", it.localizedMessage)
                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getGeofenceRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }
    companion object {
        private const val TAG = "PlanViewModel"
    }
}
