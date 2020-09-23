package com.farhanck.currencyconverter.mocks

import com.farhanck.currencyconverter.utils.AndroidTestUtils
import com.farhanck.currencyconverter.data.server.EndPoints
import io.reactivex.Single
import org.koin.core.inject
import org.koin.test.KoinTest

class MockEndpoints : EndPoints, KoinTest {
    val utils : AndroidTestUtils by inject()

    override fun currencyList(): Single<String> =
        Single.create<String> { emitter -> emitter.onSuccess(utils.getJson("currencies.json")) }


    override fun liveRates(): Single<String> =
        Single.create<String> { emitter -> emitter.onSuccess(utils.getJson("exchange_rates.json")) }
}