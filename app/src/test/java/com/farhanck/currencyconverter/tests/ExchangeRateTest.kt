package com.farhanck.currencyconverter.tests

import com.farhanck.currencyconverter.TestUtils
import com.farhanck.currencyconverter.data.db.ExchangeRate
import com.farhanck.currencyconverter.data.db.Quote
import org.junit.Assert.*
import org.junit.Test

class ExchangeRateTest  {
    val resp = TestUtils.getJson("exchange_rates.json");
    val exchangeRate = ExchangeRate.fromJsonResponse(resp);

    private fun getRateFromCurrency(quotes :List<Quote>, currency : String ) : Double {
        quotes.forEachIndexed { i, el ->
            if(el.currency.equals(currency)) return el.rate
        }
        return 0.0;
    }

    @Test
    fun fromJsonResponse() {
        assertEquals(exchangeRate.source, "USD");
    }

    @Test
    fun quoteList() {
        assertEquals(exchangeRate.quoteList.size, 168);
    }

    @Test
    fun convert() {
        val convertedRate = ExchangeRate.convert(5.0, "INR", exchangeRate);
        assertEquals(convertedRate.source, "INR");

        assertEquals(getRateFromCurrency(convertedRate.quoteList, "INR"), 5.0, 1.0);
        assertEquals(getRateFromCurrency(convertedRate.quoteList, "USD"), 0.067, 0.01);
        assertEquals(getRateFromCurrency(convertedRate.quoteList, "JPY"), 7.11, 0.01);

    }
}