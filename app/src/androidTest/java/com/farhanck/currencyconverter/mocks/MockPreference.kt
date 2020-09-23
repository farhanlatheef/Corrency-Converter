package com.farhanck.currencyconverter.mocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.farhanck.currencyconverter.data.preference.Preferences

class MockPreference : Preferences {
    private val currency = MutableLiveData<String>("USD")

    override fun getSourceCurrency(): LiveData<String> {
        return currency
    }

    override fun setSourceCurrency(currency: String) {
        this.currency.value  = currency
    }
}