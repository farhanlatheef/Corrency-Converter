package com.farhanck.currencyconverter.tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.farhanck.currencyconverter.data.db.Currency
import com.farhanck.currencyconverter.data.db.ExchangeRate
import com.farhanck.currencyconverter.data.db.Quote
import com.farhanck.currencyconverter.ui.ConverterViewModel
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.koin.core.inject
import org.koin.test.KoinTest



class ConverterViewModelTest : KoinTest {
    val converterViewModel : ConverterViewModel by inject();

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start(){
        simulateRateUpdate();
        converterViewModel.currencies.observeForever {  }
        converterViewModel.rate.observeForever {  }
    }

    private fun simulateRateUpdate() {
        converterViewModel.api.liveRates()
            .compose(converterViewModel.schedulers.applySchedulers())
            .subscribe({
                converterViewModel.rateDao.insert( ExchangeRate.fromJsonResponse(it) );
            },{});
    }

    @Test
    fun test_currencies_are_loaded() {
        assertEquals(converterViewModel.currencies.value?.size, 168);
    }

    @Test
    fun test_exchange_rate_is_loaded() {
        assertEquals(converterViewModel.rate.value?.quoteList?.size, 168)
    }

    @Test
    fun test_amount_change() {
        converterViewModel.onAmountChange("5");
        assertEquals(converterViewModel.amount.value ?: -1.0, 5.0, 1.0);
    }

    @Test
    fun test_currency_change() {
        converterViewModel.onCurrencyChange(Currency(code = "INR", name = "Indian Rupee"));
        assertEquals(converterViewModel.source.value ?: "nil", "INR");
        assertEquals(converterViewModel.rate.value?.source, "INR")
    }

    @Test
    fun test_amount_and_currency_change_reflected() {
        converterViewModel.onCurrencyChange(Currency(code = "INR", name = "Indian Rupee"));
        converterViewModel.onAmountChange("5");

        val convertedRate = converterViewModel.rate.value;
        assertEquals(getRateFromCurrency(convertedRate?.quoteList, "INR"), 5.0, 1.0);
        assertEquals(getRateFromCurrency(convertedRate?.quoteList, "USD"), 0.067, 0.01);
        assertEquals(getRateFromCurrency(convertedRate?.quoteList, "JPY"), 7.11, 0.01);
    }

    private fun getRateFromCurrency(quotes :List<Quote>?, currency : String ) : Double {
        quotes?.forEachIndexed { i, el ->
            if(el.currency.equals(currency)) return el.rate
        }
        return 0.0;
    }


}