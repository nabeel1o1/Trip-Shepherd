package com.example.tripshepherd.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.tripshepherd.repository.AuthRepository
import com.example.tripshepherd.utils.SignInState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(private val repository: AuthRepository)
    : ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> = _signInState

    fun verifyOtp(verificationId: String, otp: String) {
        _signInState.value = SignInState.Loading
        repository.verifyOtp(verificationId, otp)
            .addOnSuccessListener {
                _signInState.value = SignInState.VerificationCompleted
            }
            .addOnFailureListener { e ->
                _signInState.value = SignInState.Error(e.message ?: "OTP Verification failed")
            }
    }

    fun resendOTP(activity: Activity, mobileNo: String, resendingToken: ForceResendingToken){
        repository.resendOtp(currentActivity = activity, phoneNumber =  mobileNo, resendingToken = resendingToken,
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _signInState.value = SignInState.VerificationCompleted
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _signInState.value = SignInState.Error(e.message ?: "Verification failed")
                }

                override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                    _signInState.value = SignInState.CodeSent(verificationId, token, mobileNo)
                }
            })
    }

}