package com.sudzhaev.foodtracker

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.types.DispatchableObject
import com.sudzhaev.foodtracker.framework.*
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

class BotEntryPoint(private val basePackage: String) {
    private val log = logger<BotEntryPoint>()

    fun start() {
        val startTime = System.currentTimeMillis()

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

        val startupDuration = formatDuration(System.currentTimeMillis() - startTime)
        log.info("Bot started in $startupDuration s.")
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

    private fun Bot.Builder.setUpdatesQueue(queue: BlockingQueue<DispatchableObject>) {
        val updatesQueueField = Dispatcher::class.java.getDeclaredField("updatesQueue")
        updatesQueueField.isAccessible = true
        updatesQueueField.set(this.updater.dispatcher, queue)
    }

    private fun formatDuration(durationMillis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis)
        val millis = durationMillis - TimeUnit.SECONDS.toMillis(seconds)
        return "$seconds.$millis"
    }
}
