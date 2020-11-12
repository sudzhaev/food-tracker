package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.framework.*
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.service.FoodTrackQueryService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class GetSummaryHandler(private val foodTrackQueryService: FoodTrackQueryService)
    : MessageHandler<GetSummaryHandler.Input, GetSummaryHandler.Output>(CommandRequest("summary")) {

    override fun parseInput(chatId: IdOfChat, update: Update): Input {
        val lastDaysToGetSummary = update.commandArgs("summary")?.toLongOrNull() ?: 5L
        validate(lastDaysToGetSummary > 0) { "$PARSE_ERROR. Invalid days" }
        return Input(lastDaysToGetSummary)
    }

    override fun process(chatId: IdOfChat, input: Input): Output {
        val minDate = LocalDate.now().minusDays(input.lastNDays)
        return Output(foodTrackQueryService.listSummaryForLastNDays(chatId, minDate))
    }

    override fun respond(chatId: IdOfChat, output: Output, bot: Bot) {
        val response = StringBuilder("Summary \uD83D\uDCCB\n")
        output.dayToCaloriesMap.forEach { (date, calories) ->
            response.append("${date.asReadableString()}: $calories\n")
        }
        bot.sendMessage(chatId, response.toString())
    }

    data class Input(val lastNDays: Long)

    data class Output(val dayToCaloriesMap: SortedMap<LocalDate, Int>)
}
