package com.farhanck.currencyconverter.data.server

import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.GET




interface EndPoints {

    @GET("list")
    fun currencyList() : Single<String>

    @GET("live")
    fun liveRates() : Single<String>


    companion object {
        fun isSuccessResponse(resp : String) : String? {
            val json = JSONObject(resp);
            if(!json.has("success")) return "Server Error";
            if(!json.getBoolean("success")) {
                val errorJson = json.getJSONObject("error");
                return errorJson.getString("info");
            } else return null;
        }
    }

}