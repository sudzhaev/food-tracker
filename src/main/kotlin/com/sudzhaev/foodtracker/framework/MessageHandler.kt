package com.sudzhaev.foodtracker.framework

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.id.IdOfChat
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.util.*

private const val TRACE_ID = "traceId"

abstract class MessageHandler<INPUT, OUTPUT>(val type: RequestType) {

    protected val log = LoggerFactory.getLogger(this::class.java)!!

    open fun handle(bot: Bot, update: Update) {
        val chatId = IdOfChat(update.message?.chat?.id ?: return)
        MDC.put(TRACE_ID, UUID.randomUUID().toString())
        try {
            val input = parseInput(chatId, update)
            val output = process(chatId, input)
            respond(chatId, output, bot)
        } catch (e: Exception) {
            log.error("Error occurred", e)
            handleException(chatId, bot, e)
        } finally {
            MDC.clear()
        }
    }

    protected open fun handleException(chatId: IdOfChat, bot: Bot, e: Exception) {
        try {
            bot.sendMessage(chatId.id, """
                    Something went wrong :(
                    Error code = ${MDC.get(TRACE_ID)}
                    Please contact developer @sudzhaev (or better forward this message to him)""".trimIndent())
        } catch (ignored: Exception) {
        }
    }

    abstract fun parseInput(chatId: IdOfChat, update: Update): INPUT

    abstract fun process(chatId: IdOfChat, input: INPUT): OUTPUT

    abstract fun respond(chatId: IdOfChat, output: OUTPUT, bot: Bot)
}