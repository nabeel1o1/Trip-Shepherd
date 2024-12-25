package com.example.tripshepherd.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.tripshepherd.repository.AuthRepository
import com.example.tripshepherd.utils.SignInState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> = _signInState

    fun startPhoneVerification(phoneNumber: String, currentActivity: Activity) {
        _signInState.value = SignInState.Loading
        repository.startPhoneNumberVerification(phoneNumber, currentActivity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _signInState.value = SignInState.VerificationCompleted
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _signInState.value = SignInState.Error(e.message ?: "Verification failed")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                _signInState.value = SignInState.CodeSent(verificationId, token, phoneNumber)
            }
        })
    }

    fun signInWithGoogle(idToken: String) {
        _signInState.value = SignInState.Loading
        repository.signInWithGoogle(idToken,
            onSuccess = { authResult ->
                _signInState.value = SignInState.GoogleSignInSuccess(authResult.user)
            },
            onError = { exception ->
                _signInState.value = SignInState.Error(exception.message ?: "Unknown error")
            }
        )
    }

    fun getGoogleSignInIntent() = repository.getGoogleSignInIntent()
}