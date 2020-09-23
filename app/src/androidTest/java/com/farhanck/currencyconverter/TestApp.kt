package com.farhanck.currencyconverter

import android.app.Application
import com.farhanck.currencyconverter.mocks.utilModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApp)
            modules(
                com.farhanck.currencyconverter.mocks.networkModule,
                com.farhanck.currencyconverter.mocks.roomModule,
                com.farhanck.currencyconverter.mocks.viewModelModule,
                com.farhanck.currencyconverter.mocks.preferencesModule, utilModules
            )
        }
    }
}


