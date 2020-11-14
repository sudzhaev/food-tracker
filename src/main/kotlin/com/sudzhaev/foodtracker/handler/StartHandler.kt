package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.framework.CommandRequest
import com.sudzhaev.foodtracker.framework.MessageHandler
import com.sudzhaev.foodtracker.framework.sendMessage
import com.sudzhaev.foodtracker.id.IdOfChat
import org.springframework.stereotype.Component

@Component
class StartHandler : MessageHandler<Any?, String>(CommandRequest("start")) {

    override fun parseInput(chatId: IdOfChat, update: Update): Any? {
        return null
    }

    override fun process(chatId: IdOfChat, input: Any?): String {
        return """
            This bot helps you track how much calories you consume every day.
            Just send message in format '{food name} {number of calories}'.
            For example:
            - avocado 150
            - pizza pepperoni 700
            To get statistics use /summary and /stats commands
            To get detailed reference use /help
            """.trimIndent()
    }

    override fun respond(chatId: IdOfChat, output: String, bot: Bot) {
        bot.sendMessage(chatId, output)
    }
}
