package com.example.tripshepherd.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.tripshepherd.repository.AuthRepository
import com.example.tripshepherd.utils.VerificationState
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

    private val _otpVerificationState = MutableStateFlow<VerificationState>(VerificationState.Idle)
    val otpVerificationState: StateFlow<VerificationState> = _otpVerificationState

    fun verifyOtp(verificationId: String, otp: String) {
        _otpVerificationState.value = VerificationState.Loading
        repository.verifyOtp(verificationId, otp)
            .addOnSuccessListener {
                _otpVerificationState.value = VerificationState.VerificationCompleted
            }
            .addOnFailureListener { e ->
                _otpVerificationState.value = VerificationState.Error(e.message ?: "OTP Verification failed")
            }
    }

    fun resendOTP(activity: Activity, mobileNo: String, resendingToken: ForceResendingToken){
        repository.resendOtp(currentActivity = activity, phoneNumber =  mobileNo, resendingToken = resendingToken,
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _otpVerificationState.value = VerificationState.VerificationCompleted
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _otpVerificationState.value = VerificationState.Error(e.message ?: "Verification failed")
                }

                override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                    _otpVerificationState.value = VerificationState.CodeSent(verificationId, token, mobileNo)
                }
            })
    }

}