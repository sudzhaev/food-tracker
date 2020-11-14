package com.sudzhaev.foodtracker.framework

sealed class RequestType

object TextRequest : RequestType() {
    override fun toString(): String {
        return this::class.simpleName!!
    }
}

data class CommandRequest(val command: String) : RequestType()
