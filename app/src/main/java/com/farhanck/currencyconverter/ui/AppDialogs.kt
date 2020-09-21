package com.farhanck.currencyconverter.ui

import android.app.AlertDialog
import android.content.Context

class AppDialogs {

    companion object {
        fun showAlert(context: Context?, msg: String, positive: String, listener : Positive) {
            val alertDialogBuilder =  AlertDialog.Builder(context)
            alertDialogBuilder.setMessage(msg)
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton(positive) { arg0, arg1 ->  listener.onPositive() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    interface Positive {
        fun onPositive();
    }
}