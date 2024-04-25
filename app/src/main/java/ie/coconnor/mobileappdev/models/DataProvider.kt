package ie.coconnor.mobileappdev.models


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseUser
import ie.coconnor.mobileappdev.models.Response.Success

enum class AuthState {
    Authenticated, SignedIn, SignedOut;
}

object DataProvider {

    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Success(null))

    var anonymousSignInResponse by mutableStateOf<FirebaseSignInResponse>(Success(null))

    var googleSignInResponse by mutableStateOf<FirebaseSignInResponse>(Success(null))

    var signOutResponse by mutableStateOf<SignOutResponse>(Success(false))

    var user by mutableStateOf<FirebaseUser?>(null)

    var isAuthenticated by mutableStateOf(false)

    var isAnonymous by mutableStateOf(false)

    var authState by mutableStateOf(AuthState.SignedOut)
        private set

    fun updateAuthState(user: FirebaseUser?) {
        this.user = user
        isAuthenticated = user != null
        isAnonymous = user?.isAnonymous ?: false

        authState = if (isAuthenticated) {
            if (isAnonymous) AuthState.Authenticated else AuthState.SignedIn
        } else {
            AuthState.SignedOut
        }
    }

    fun getDisplayName(user: FirebaseUser?): String{
        this.user = user
        val displayName = user!!.displayName
        return displayName.toString()
    }

    fun getProfilePhoto(user: FirebaseUser?): String{
        this.user = user
        val photoUrl = user!!.photoUrl
        return photoUrl.toString().replace("s96", "s250")
    }
}