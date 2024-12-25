package com.example.tripshepherd.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.IOException

fun countryList(context: Context): MutableList<Country> {
    val jsonFileString = getJsonDataFromAsset(context, "Countries.json")
    val type = object : TypeToken<List<Country>>() {}.type
    return Gson().fromJson(jsonFileString, type)
}

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun List<Country>.searchCountry(countryName: String): List<Country> {
    val searchTerm = countryName.lowercase()
    return filter { country ->
        country.name.lowercase().startsWith(searchTerm) ||
                country.code.lowercase().startsWith(searchTerm)
    }
}

data class Country(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("dial_code")
    @Expose
    val dialCode: String = "",
    @SerializedName("code")
    @Expose
    val code: String = ""
)