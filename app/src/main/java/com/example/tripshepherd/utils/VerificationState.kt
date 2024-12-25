package com.example.tripshepherd.utils

import com.google.firebase.auth.PhoneAuthProvider

sealed class VerificationState {
    data object Idle : VerificationState()
    data object Loading : VerificationState()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken,
        val phoneNo: String
    ) : VerificationState()
    data object VerificationCompleted : VerificationState()
    data class Error(val message: String) : VerificationState()
}