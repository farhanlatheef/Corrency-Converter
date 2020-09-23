package com.farhanck.currencyconverter.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class Quote(val currency:String, val rate : Double)

@Entity(
    tableName = "exchangeRates",
    indices = [Index(value = ["source"], unique = true)]
)
data class ExchangeRate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp")  val timestamp:Long,
    @ColumnInfo(name = "source")  val source:String,
    @ColumnInfo(name = "quotes")  val quotes:String
) {
    @Ignore
    var quoteList = quoteList(source, quotes);

    fun lastUpdated() :String{
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
        val str = sdf.format(Date(timestamp)) // *1000
        return "last updated : $str";
    }

    companion object {
        fun fromJsonResponse(resp: String): ExchangeRate {
            val json = JSONObject(resp);
            val quotes = json.getJSONObject("quotes");
            val source = json.getString("source");
            val timestamp = Calendar.getInstance().timeInMillis;  // json.getLong("timestamp");

            return ExchangeRate(
                timestamp = timestamp,
                source = source,
                quotes = quotes.toString()
            )
        }

         fun quoteList(source:String, quotes:String) : List<Quote> {
            val quoteList = arrayListOf<Quote>()
            val json = JSONObject(quotes);
            val keys: Iterator<String> = json.keys()
            while (keys.hasNext()) {
                val key = keys.next();
                quoteList.add(Quote( key.substring(source.length), json.getDouble(key) ))
            }
            return quoteList;
        }

        fun convert(amount:Double, newSource:String, rate : ExchangeRate?) : ExchangeRate  {
            if(rate == null) {
                return ExchangeRate (
                    timestamp = 0,
                    source = newSource,
                    quotes = "{}"
                )
            }
            if(newSource.equals(rate.source)) return rate;

            var scale = 1.0;
            for(item in rate.quoteList) {
                if(item.currency.equals(newSource)) {
                    scale = item.rate; break;
                }
            }

            val quoteList = arrayListOf<Quote>()
            for(item in rate.quoteList) quoteList.add(Quote(item.currency, amount * (item.rate/scale) ))

            val newRate = ExchangeRate (
                timestamp = rate.timestamp,
                source = newSource,
                quotes = "{}"
            )
            newRate.quoteList = quoteList;
            return newRate;
        }
    }


}

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchangeRates where source=(:source)")
    fun get(source : String): LiveData<ExchangeRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rate : ExchangeRate)

    @Query("UPDATE  exchangeRates set quotes=(:quotes), timestamp=(:timestamp) where source=(:source)")
    fun update(source : String, timestamp: Long, quotes: String)


    @Query("DELETE FROM exchangeRates")
    fun deleteAll()

}