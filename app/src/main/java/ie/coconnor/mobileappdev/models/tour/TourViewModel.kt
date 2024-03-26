package ie.coconnor.mobileappdev.models.tour

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.coconnor.mobileappdev.repository.TourRepository
import kotlinx.coroutines.launch


class TourViewModel : ViewModel() {
    private val repository = TourRepository()

    private val _tours = MutableLiveData<TourResponse>()
    val tours: LiveData<TourResponse> = _tours

    fun fetchTours() {
        viewModelScope.launch {
            try {
                val cards = repository.getTours()
                _tours.value = cards
                Log.e("FetchCreditCard", _tours.value.toString());
            } catch (e: Exception) {
                // Handle error
                Log.e("FetchCreditCard", e.message.toString());
            }
        }
    }
}