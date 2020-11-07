package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.framework.MessageHandler
import com.sudzhaev.foodtracker.framework.TextRequest
import com.sudzhaev.foodtracker.framework.text
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.service.FoodTrackEditService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AddMessageHandler(private val foodTrackEditService: FoodTrackEditService)
    : MessageHandler<FoodTrack?, String?>(TextRequest) {

    override fun parseInput(chatId: IdOfChat, update: Update): FoodTrack? {
        val text = update.text ?: return null
        val lastSpaceIndex = text.lastIndexOf(" ")
        if (lastSpaceIndex < 0) {
            return null
        }
        val name = text.substring(0, lastSpaceIndex).trim()
        if (name.isBlank()) {
            return null
        }
        val calories = text.substring(lastSpaceIndex + 1).trim().toIntOrNull() ?: return null
        return FoodTrack(name, calories, LocalDateTime.now())
    }

    override fun process(chatId: IdOfChat, input: FoodTrack?): String? {
        if (input == null) {
            return "Cannot parse message"
        }
        foodTrackEditService.save(chatId, input)
        return null
    }

    override fun respond(chatId: IdOfChat, output: String?, bot: Bot) {
        if (output != null) {
            bot.sendMessage(chatId.id, output)
        }
    }
}
