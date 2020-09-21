package com.farhanck.currencyconverter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.farhanck.currencyconverter.core.*
import com.farhanck.currencyconverter.data.db.Currency
import com.farhanck.currencyconverter.data.db.CurrencyDao
import com.farhanck.currencyconverter.data.db.ExchangeRate
import com.farhanck.currencyconverter.data.db.ExchangeRateDao
import com.farhanck.currencyconverter.data.server.EndPoints
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class ConverterViewModel(
    val currencyDao : CurrencyDao,
    val rateDao: ExchangeRateDao,
    val api : EndPoints,
    val pref : Preferences
) : BaseViewModel() {
    val amount = MutableLiveData<Double>(1.0);


    val currencies : LiveData<List<Currency>> = Transformations.map(currencyDao.getAll()) { data ->
        if(data.size == 0) fetchCurrencies();
        data
    }

    val source = pref.getSourceCurrency();

    val rate : LiveData<ExchangeRate> = Transformations.switchMap(DoubleTrigger(amount, source)) { result ->
        Transformations.map(rateDao.get("USD")) { data ->
           ExchangeRate.convert(result.first!!, result.second!!, data)
        }
    }

    private val _errorToShow = MutableLiveData<UIEvent<String>>()
    val errorToShow : LiveData<UIEvent<String>>
        get() = _errorToShow


    private fun fetchCurrencies() {
        addToDisposable(api.currencyList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                val error = EndPoints.isSuccessResponse(it);
                if(error != null)  _errorToShow.value = UIEvent(error);
                else ioThread {  currencyDao.insertAll( Currency.fromJsonResponse(it) ) }
            }, {
                _errorToShow.value = UIEvent(it.message ?: "unknown error");
            }));
    }

    fun onAmountChange(str: CharSequence) {
        val amountStr = str.toString()
        try {
            this.amount.value = (if(amountStr.isEmpty()) "0" else amountStr) .toDouble();
        } catch (ex : NumberFormatException) {
            this.amount.value = 0.0;
        }
    }

    fun onCurrencyChange(currency: Currency) {
        pref.setSourceCurrency(currency.code);
    }

}