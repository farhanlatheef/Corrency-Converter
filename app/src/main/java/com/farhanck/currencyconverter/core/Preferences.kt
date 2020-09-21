package com.farhanck.currencyconverter.core

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class Preferences(val pref : SharedPreferences) {
    companion object {
        val SOURCE_CURRENCY = "source_currency";
    }


    fun getSourceCurrency(): LiveData<String> = SharedPreferenceStringLiveData(pref, SOURCE_CURRENCY, "USD")
    fun setSourceCurrency(currency : String) = pref.edit().putString(SOURCE_CURRENCY, currency).apply()

}