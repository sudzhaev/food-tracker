package com.sudzhaev.foodtracker.framework

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.HandleUpdate
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandler
import com.github.kotlintelegrambot.dispatcher.handlers.Handler

class MessageHandlerDispatcher(private val handlers: List<MessageHandler<*, *>>) {
    private val log = logger<MessageHandlerDispatcher>()

    init {
        require(handlers.isNotEmpty()) { "Bot has no handlers to dispatch" }
    }

    fun apply(builder: Bot.Builder) {
        log.info("Initializing message dispatching")
        handlers.forEach {
            log.info(it.loggable())
            builder.updater.dispatcher.addHandler(it.asNativeHandler())
        }
    }

    private fun MessageHandler<*, *>.asNativeHandler(): Handler {
        return when (type) {
            is CommandRequest -> CommandHandler(type.command, asHandleUpdate())
            TextRequest -> PlainTextNativeHandler(asHandleUpdate())
        }
    }

    private fun MessageHandler<*, *>.asHandleUpdate(): HandleUpdate {
        return { bot, update -> handle(bot, update) }
    }

    private fun MessageHandler<*, *>.loggable(): String {
        return "${this::class.simpleName!!}(${this.type})"
    }
}
