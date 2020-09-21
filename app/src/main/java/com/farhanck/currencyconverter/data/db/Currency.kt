package com.farhanck.currencyconverter.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.*
import io.reactivex.Single
import org.json.JSONObject
import java.util.*


@Entity(
    tableName = "currencies",
    indices = [Index(value = ["code"], unique = true)]
)
data class Currency(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "code")  val code:String,
    @ColumnInfo(name = "name")  val name:String
    ) {

    companion object {


        fun fromJsonResponse(resp : String):List<Currency> {
            val list = arrayListOf<Currency>()
            val json = JSONObject(resp).getJSONObject("currencies");
            val keys: Iterator<String> = json.keys()

            while (keys.hasNext()) {
                val key = keys.next();
                list.add(Currency(code = key, name = json.getString(key) ))
            }
            return list;
        }

        fun getSpinnerPosition(code :String, list : List<Currency>) : Int {
            list.forEachIndexed { index, item ->
                if(item.code.equals(code)) return index;
            }
            return 0;
        }
    }

    override fun toString(): String {
        return "$code - $name"
    }

}


@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies")
    fun getAll(): LiveData<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(methods: List<Currency>)

    @Query("DELETE FROM currencies")
    fun deleteAll()

}
