package com.farhanck.currencyconverter.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farhanck.currencyconverter.data.db.AppDatabase.Companion.DB_VERSION


@Database(
    entities = [ExchangeRate::class, Currency::class],
    version = DB_VERSION,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract  fun getCurrencyDao(): CurrencyDao
    abstract fun getExchangeRateDao(): ExchangeRateDao

    companion object {
        const val DB_VERSION = 1
        private const val DB_NAME = "cl_db.db"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
//                .addMigrations(MIGRATION_1_TO_2)
                .build()

//        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//            }
//        }
    }
}