package com.sudzhaev.foodtracker.framework

import java.time.LocalDateTime
import java.util.concurrent.LinkedBlockingQueue

class TimeAwareLinkedBlockingQueue<T> : LinkedBlockingQueue<T>() {
    var lastTakeTime: LocalDateTime? = null
    var lastPutTime: LocalDateTime? = null

    override fun take(): T {
        lastTakeTime = LocalDateTime.now()
        return super.take()
    }

    override fun put(e: T) {
        lastPutTime = LocalDateTime.now()
        super.put(e)
    }
}
