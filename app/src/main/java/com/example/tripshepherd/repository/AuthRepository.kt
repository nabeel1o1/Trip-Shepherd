package com.example.tripshepherd.repository

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) {

    fun startPhoneNumberVerification(
        phoneNumber: String,
        currentActivity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(15L, TimeUnit.SECONDS)
            .setActivity(currentActivity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOtp(
        phoneNumber: String,
        currentActivity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        resendingToken: ForceResendingToken
    ){
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(15L, TimeUnit.SECONDS)
            .setActivity(currentActivity)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendingToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(verificationId: String, otp: String): Task<AuthResult> {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        return firebaseAuth.signInWithCredential(credential)
    }

    fun signInWithGoogle(idToken: String, onSuccess: (AuthResult) -> Unit, onError: (Exception) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { onSuccess(it) }
                } else {
                    task.exception?.let { onError(it) }
                }
            }
    }

    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

}