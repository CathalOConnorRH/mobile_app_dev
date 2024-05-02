package ie.coconnor.mobileappdev.repository

import com.google.android.gms.auth.api.identity.SignInCredential
import ie.coconnor.mobileappdev.models.AuthStateResponse
import ie.coconnor.mobileappdev.models.FirebaseSignInResponse
import ie.coconnor.mobileappdev.models.OneTapSignInResponse
import ie.coconnor.mobileappdev.models.SignOutResponse
import kotlinx.coroutines.CoroutineScope

interface AuthRepository {

    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse

    suspend fun signInAnonymously(): FirebaseSignInResponse

    suspend fun onTapSignIn(): OneTapSignInResponse

    suspend fun signInWithGoogle(credential: SignInCredential): FirebaseSignInResponse

    suspend fun signOut(): SignOutResponse
}