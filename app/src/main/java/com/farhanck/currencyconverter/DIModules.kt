package com.farhanck.currencyconverter

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import com.farhanck.currencyconverter.core.Preferences
import com.farhanck.currencyconverter.core.SharedPreferenceLiveData
import com.farhanck.currencyconverter.core.SharedPreferenceStringLiveData
import com.farhanck.currencyconverter.data.db.AppDatabase
import com.farhanck.currencyconverter.data.db.CurrencyDao
import com.farhanck.currencyconverter.data.server.EndPoints
import com.farhanck.currencyconverter.ui.ConverterViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


val networkModule = module {

    single {
        Interceptor { chain ->
            var request = chain.request();
            val url = request.url().newBuilder()
                .addQueryParameter("access_key", BuildConfig.CL_API_KEY).build()

            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }
    single { OkHttpClient.Builder().addInterceptor(get()).build() }

    single {
        Retrofit.Builder()
            .baseUrl("http://apilayer.net/")
            .addConverterFactory(ScalarsConverterFactory.create()) // since the data is available key/value, we are passing manually
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }

    single(createdAtStart = false) { get<Retrofit>().create(EndPoints::class.java) }
//    single { RxSingleSchedulers.DEFAULT }
}


val roomModule = module {
    single { AppDatabase.getInstance(androidApplication()) }
    single(createdAtStart = false) { get<AppDatabase>().getCurrencyDao() }
    single(createdAtStart = false) { get<AppDatabase>().getExchangeRateDao() }
}

val viewModelModule = module {
    viewModel { ConverterViewModel(get() , get(), get(), get()) } //
}




private const val PREFERENCES_FILE_KEY = "cl_preferences"
private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)



val preferencesModule = module {
    single { provideSettingsPreferences(androidApplication()) }
    single { Preferences(get()) }
}