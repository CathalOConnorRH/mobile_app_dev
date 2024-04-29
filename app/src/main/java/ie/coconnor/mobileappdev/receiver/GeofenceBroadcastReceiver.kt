package ie.coconnor.mobileappdev.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import ie.coconnor.mobileappdev.MainActivity
import ie.coconnor.mobileappdev.R
import timber.log.Timber

class GeofenceBroadcastReceiver : BroadcastReceiver() {

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
                sendNotification(context!!,"Enterance")
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                sendNotification(context!!,"Dwell")
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                sendNotification(context!!, "Exit")
            }
            else -> {
                Timber.tag(TAG).i("Error in setting up the geofence")
            }
        }
    }

    private fun sendNotification(context: Context, type: String) {

        val notificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager

        //This is to create a Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val mChannel =
                NotificationChannel(CHANNEL_ID, "Channel1", NotificationManager.IMPORTANCE_HIGH)
            mChannel.enableLights(true)
            mChannel.lightColor = Color.GREEN
            mChannel.enableVibration(false)
            notificationManager.createNotificationChannel(mChannel)

        }

        /*
        * Intent to send in the Notification
        *
        * */
        val contentIntent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )


        /*
        * Notification Builder
        * */

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("GeoFence App")
            .setContentText("You have $type the geofence area")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setLights(ContextCompat.getColor(context, R.color.purple_200), 50, 10)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        notificationManager.notify(1234, notification)

    }

    companion object {
        private const val TAG = "GeofenceBroadcastReciever"
        private const val CHANNEL_ID = "Geofence Channel"

    }
}