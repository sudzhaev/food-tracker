package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.framework.*
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.service.FoodTrackEditService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AddMessageHandler(private val foodTrackEditService: FoodTrackEditService)
    : MessageHandler<FoodTrack, Any?>(TextRequest) {

    override fun parseInput(chatId: IdOfChat, update: Update): FoodTrack {
        val text = update.text ?: raise(PARSE_ERROR)
        val lastSpaceIndex = text.lastIndexOf(" ")
        validate(lastSpaceIndex > 0) { "$PARSE_ERROR. Invalid format '{name} {calories}'" }

        val name = text.substring(0, lastSpaceIndex).trim()
        validate(name.isNotBlank()) { "$PARSE_ERROR. Name is blank" }

        val calories = text.substring(lastSpaceIndex + 1).trim().toIntOrNull()
                ?: raise("$PARSE_ERROR. Calories must be number")
        validate(calories > 0) { "$PARSE_ERROR. Calories are negative" }
        // TODO: can I use time from update?
        return FoodTrack(name, calories, LocalDateTime.now())
    }

    override fun process(chatId: IdOfChat, input: FoodTrack): Any? {
        foodTrackEditService.save(chatId, input)
        return null
    }

    override fun respond(chatId: IdOfChat, output: Any?, bot: Bot) {
        // no-op
    }
}
