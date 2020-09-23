package com.farhanck.currencyconverter.data.preference

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class AppPreferences(val pref : SharedPreferences) : Preferences {
    companion object {
        val SOURCE_CURRENCY = "source_currency";
    }


    override fun getSourceCurrency(): LiveData<String> = SharedPreferenceStringLiveData(pref, SOURCE_CURRENCY, "USD")
    override fun setSourceCurrency(currency : String) = pref.edit().putString(SOURCE_CURRENCY, currency).apply()

}