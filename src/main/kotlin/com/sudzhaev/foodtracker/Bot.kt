package com.sudzhaev.foodtracker

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.types.DispatchableObject
import com.sudzhaev.foodtracker.framework.*
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.concurrent.BlockingQueue

class Bot(private val basePackage: String) {
    private val log = logger<Bot>()

    fun start() {
        log.info("Starting bot")
        val ctx = AnnotationConfigApplicationContext(basePackage)
        log.info("Context initialized")
        val handlers = ctx.listBeans<MessageHandler<*, *>>()
        val dispatcher = MessageHandlerDispatcher(handlers)
        val queue = TimeAwareLinkedBlockingQueue<DispatchableObject>()
        val bot = bot {
            token = System.getProperty("token")
            logLevel = HttpLoggingInterceptor.Level.valueOf(System.getProperty("apiClient.logging", "NONE"))
            setUpdatesQueue(queue)
            dispatcher.apply(this)
        }
        startHealthCheckThread(queue)
        log.info("Bot started")
        bot.startPolling()
    }

    private fun startHealthCheckThread(queue: TimeAwareLinkedBlockingQueue<DispatchableObject>) {
        log.info("Starting healthCheck thread")
        val healthCheckThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                log.info("Health: lastTakeTime = ${queue.lastTakeTime}, lastPutTime = ${queue.lastPutTime}")
                Thread.sleep(60000)
            }
        }
        healthCheckThread.name = "healthCheck"
        healthCheckThread.isDaemon = true
        healthCheckThread.start()
    }

    private fun com.github.kotlintelegrambot.Bot.Builder.setUpdatesQueue(queue: BlockingQueue<DispatchableObject>) {
        val updatesQueueField = Dispatcher::class.java.getDeclaredField("updatesQueue")
        updatesQueueField.isAccessible = true
        updatesQueueField.set(this.updater.dispatcher, queue)
    }
}
