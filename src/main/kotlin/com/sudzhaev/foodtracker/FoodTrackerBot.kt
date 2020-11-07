package com.sudzhaev.foodtracker

import com.github.kotlintelegrambot.bot
import com.sudzhaev.foodtracker.framework.MessageHandler
import com.sudzhaev.foodtracker.framework.MessageHandlerDispatcher
import com.sudzhaev.foodtracker.framework.listBeans
import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun main() {
    val ctx = AnnotationConfigApplicationContext("com.sudzhaev.foodtracker")
    val handlers = ctx.listBeans<MessageHandler<*, *>>()
    val dispatcher = MessageHandlerDispatcher(handlers)
    val bot = bot {
        token = System.getProperty("token")
        dispatcher.apply(this)
    }
    bot.startPolling()
}
