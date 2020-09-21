package com.farhanck.currencyconverter.data.server

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.farhanck.currencyconverter.data.db.ExchangeRate
import com.farhanck.currencyconverter.data.db.ExchangeRateDao
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class ExchangeRateUpdater(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams), KoinComponent {
    val rateDao: ExchangeRateDao by inject()
    val api : EndPoints by inject()

    override fun doWork(): Result {
       val ob = api.liveRates()
            .subscribeOn(Schedulers.io())
            .subscribe({
                val error = EndPoints.isSuccessResponse(it);
                if(error == null)
                    rateDao.insert(ExchangeRate.fromJsonResponse(it))
            }, {
                Log.e("RateUpdater:Error", it.message ?: "unknown error");
            })
        return Result.success();
    }
}