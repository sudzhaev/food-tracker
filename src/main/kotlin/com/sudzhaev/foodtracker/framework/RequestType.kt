package com.sudzhaev.foodtracker.framework

sealed class RequestType
object TextRequest : RequestType()
data class CommandRequest(val command: String) : RequestType()
