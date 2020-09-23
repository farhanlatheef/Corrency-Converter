package com.farhanck.currencyconverter.mocks

import android.content.Context
import com.farhanck.currencyconverter.core.RxSingleSchedulers
import com.farhanck.currencyconverter.data.server.EndPoints
import com.farhanck.currencyconverter.ui.ConverterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.farhanck.currencyconverter.utils.AndroidTestUtils
import com.farhanck.currencyconverter.data.db.TestDb
import com.farhanck.currencyconverter.data.preference.Preferences


val networkModule = module {
    single { MockEndpoints() as EndPoints }
}

val roomModule = module {
//    val appContext = ApplicationProvider.getApplicationContext<Context>()
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    single { TestDb.getInstance(appContext) }
    single(createdAtStart = false) { get<TestDb>().getCurrencyDao() }
    single(createdAtStart = false) { get<TestDb>().getExchangeRateDao() }

}

val preferencesModule = module {
    single { MockPreference() as Preferences }
}

val viewModelModule = module {
    viewModel { ConverterViewModel(get() , get(), get(), get(), RxSingleSchedulers.TEST_SCHEDULER) }
}

val utilModules = module {

    single { AndroidTestUtils(ApplicationProvider.getApplicationContext<Context>()) }
}