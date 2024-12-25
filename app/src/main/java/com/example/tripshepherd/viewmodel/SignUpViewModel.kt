package com.example.tripshepherd.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.tripshepherd.repository.AuthRepository
import com.example.tripshepherd.utils.VerificationState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _verificationState = MutableStateFlow<VerificationState>(VerificationState.Idle)
    val verificationState: StateFlow<VerificationState> = _verificationState

    fun startPhoneVerification(phoneNumber: String, currentActivity: Activity) {
        _verificationState.value = VerificationState.Loading
        repository.startPhoneNumberVerification(phoneNumber, currentActivity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _verificationState.value = VerificationState.VerificationCompleted
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _verificationState.value = VerificationState.Error(e.message ?: "Verification failed")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                _verificationState.value = VerificationState.CodeSent(verificationId, token, phoneNumber)
            }
        })
    }

}