package com.farhanck.currencyconverter

import java.io.File

class TestUtils {
    companion object {
        fun getJson(path : String) : String {
            val uri = javaClass.classLoader!!.getResource(path)
            val file = File(uri.path)
            return String(file.readBytes())
        }
    }
}