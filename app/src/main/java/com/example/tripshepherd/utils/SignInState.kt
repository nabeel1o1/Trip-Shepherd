package com.example.tripshepherd.utils

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider

sealed class SignInState {
    data object Idle : SignInState()
    data object Loading : SignInState()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken,
        val phoneNo: String
    ) : SignInState()
    data object VerificationCompleted : SignInState()
    data class GoogleSignInSuccess(val user: FirebaseUser?) : SignInState()
    data class Error(val message: String) : SignInState()
}