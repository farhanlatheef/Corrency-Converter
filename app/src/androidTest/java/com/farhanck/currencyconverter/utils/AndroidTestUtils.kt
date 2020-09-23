package com.farhanck.currencyconverter.utils

import android.content.Context
import android.util.Log
import java.io.File

class AndroidTestUtils(val context: Context) {

    fun getJson( path : String) : String {
        context.assets.open(path);
        val inputStream = context.assets.open(path);
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charsets.UTF_8)
    }

}