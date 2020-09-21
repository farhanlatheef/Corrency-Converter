package com.farhanck.currencyconverter.core

import java.util.concurrent.Executors


private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun ioThread(f : () -> Unit) = IO_EXECUTOR.execute(f)