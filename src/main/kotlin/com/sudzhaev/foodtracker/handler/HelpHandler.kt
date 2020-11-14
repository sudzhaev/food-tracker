package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.framework.CommandRequest
import com.sudzhaev.foodtracker.framework.MessageHandler
import com.sudzhaev.foodtracker.framework.sendMessage
import com.sudzhaev.foodtracker.id.IdOfChat
import org.springframework.stereotype.Component

@Component
class HelpHandler : MessageHandler<Any?, String>(CommandRequest("help")) {

    private val foodEmojis = setOf(
            "🍳", "🍷", "🍎", "🍕", "🍣",
    )

    override fun parseInput(chatId: IdOfChat, update: Update): Any? {
        return null
    }

    override fun process(chatId: IdOfChat, input: Any?): String {
        return """
        <b>Food tracker bot</b> ${foodEmojis.random()}
                  
        To add new record send message in format '{food name} {number of calories}'
        For example:
        - avocado 150
        - pizza pepperoni 700
        
        Summary – number of calories you consumed per day
        /summary – Get summary for last 5 days
        /summary N – Get summary for last N days
        /summary YYYY-MM-DD – Get summary for date
        
        Statistics – food and number of calories you consumed per day 
        /stats – Get statistics for last 5 days
        /stats N – Get statistics for last N days
        /stats YYYY-MM-DD – Get statistics for date
        """.trimIndent()
    }

    override fun respond(chatId: IdOfChat, output: String, bot: Bot) {
        bot.sendMessage(chatId, output, parseMode = ParseMode.HTML)
    }
}
