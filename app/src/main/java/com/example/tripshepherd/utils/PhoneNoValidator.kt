package com.example.tripshepherd.utils

import android.content.Context
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

class PhoneNoValidator(private val context: Context) {

    private val phoneUtil: PhoneNumberUtil by lazy { PhoneNumberUtil.createInstance(context) }

    operator fun invoke(number: String, countryCode: String): Boolean {
        val fullNumber = "$countryCode$number"
        return try {
            val phoneNumber = phoneUtil.parse(fullNumber, null)
            phoneUtil.isValidNumber(phoneNumber)
        } catch (e: Exception) {
            false
        }
    }


}


