package com.sudzhaev.foodtracker.framework

class RequestProcessingException(val responseMessage: String) : Throwable(responseMessage)

fun raise(responseMessage: String): Nothing {
    throw RequestProcessingException(responseMessage)
}

inline fun validate(condition: Boolean, lazyMessage: () -> String) {
    if (!condition) {
        raise(lazyMessage())
    }
}
