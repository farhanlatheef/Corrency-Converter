package com.farhanck.currencyconverter.tests

import com.farhanck.currencyconverter.TestUtils
import com.farhanck.currencyconverter.data.db.Currency
import org.junit.Assert.*
import org.junit.Test


class CurrencyTest {

    val resp = TestUtils.getJson("currencies.json");
    val currencyList = Currency.fromJsonResponse(resp);



    @Test
    fun fromJsonResponse() {
        assertEquals(currencyList.size, 168);
    }

    @Test
    fun getListIndex() {
        val currencyList = Currency.fromJsonResponse(resp);
        assertEquals(Currency.getListIndex("USD",currencyList), 63);
    }

}