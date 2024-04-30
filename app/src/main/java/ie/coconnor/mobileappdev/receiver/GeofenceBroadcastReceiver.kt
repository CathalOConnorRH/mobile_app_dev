package ie.coconnor.mobileappdev.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import timber.log.Timber
import java.util.Locale

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private lateinit var textToSpeech: TextToSpeech

    override fun onReceive(context: Context?, intent: Intent?) {
        //Added Notification
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent?.hasError() == true) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Timber.tag(TAG).e( errorMessage)
            return
        }
        // Get the transition type.
        when (geofencingEvent?.geofenceTransition) {

            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                textToSpeech = TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        val result = textToSpeech.setLanguage(Locale.US)
                        if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED
                        ) {
                            println("language is not supported")
                            Toast.makeText(context, "language is not supported", Toast.LENGTH_LONG).show()
                        }
                    }
                    textToSpeech.speak(intent.extras?.getString("tripName"), TextToSpeech.QUEUE_FLUSH, null, null)
                    textToSpeech.speak(intent.extras?.getString("tripDescription"), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
            else -> {
                Timber.tag(TAG).i("Error in setting up the geofence")
            }
        }
    }

    companion object {
        private const val TAG = "GeofenceBroadcastReceiver"
    }
}