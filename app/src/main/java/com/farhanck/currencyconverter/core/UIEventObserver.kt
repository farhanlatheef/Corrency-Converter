package com.farhanck.currencyconverter.core

import androidx.lifecycle.Observer


open class UIEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

class UIEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<UIEvent<T>> {
    override fun onChanged(event: UIEvent<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}