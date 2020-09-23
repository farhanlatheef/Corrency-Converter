package com.farhanck.currencyconverter.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [ExchangeRate::class, Currency::class],
    version = TestDb.DB_VERSION,
    exportSchema = false
)
abstract class TestDb : RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
    abstract fun getExchangeRateDao(): ExchangeRateDao

    companion object {
        const val DB_VERSION = 1
        @Volatile
        private var INSTANCE: TestDb? = null

        fun getInstance(context: Context): TestDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.inMemoryDatabaseBuilder(context.applicationContext, TestDb::class.java)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}