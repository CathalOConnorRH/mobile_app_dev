package ie.coconnor.mobileappdev.models

import com.google.firebase.firestore.FirebaseFirestore


object Constants {
    const val SIGN_IN_REQUEST = "signInRequest"
    const val SIGN_UP_REQUEST = "signUpRequest"
    const val CUSTOM_INTENT_GEOFENCE = "GEOFENCE-TRANSITION-INTENT-ACTION"
    const val CUSTOM_REQUEST_CODE_GEOFENCE = 1001
    const val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 456
    const val LOCATION_PERMISSION_REQUEST_CODE = 123

    val db = FirebaseFirestore.getInstance()
    var tripAdvisorApiKey = ""

    object AuthErrors {
        const val CREDENTIAL_ALREADY_IN_USE = "ERROR_CREDENTIAL_ALREADY_IN_USE"
        const val EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    }
}