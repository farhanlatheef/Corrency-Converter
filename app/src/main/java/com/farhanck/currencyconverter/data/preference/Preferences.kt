package com.farhanck.currencyconverter.data.preference

import androidx.lifecycle.LiveData

interface Preferences {
    fun getSourceCurrency(): LiveData<String>
    fun setSourceCurrency(currency : String)
}