package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.framework.CommandRequest
import com.sudzhaev.foodtracker.framework.MessageHandler
import com.sudzhaev.foodtracker.framework.text
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.service.FoodTrackQueryService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Component
class GetSummaryHandler(private val foodTrackQueryService: FoodTrackQueryService)
    : MessageHandler<GetSummaryHandler.Input, GetSummaryHandler.Output>(CommandRequest("summary")) {

    override fun parseInput(chatId: IdOfChat, update: Update): Input {
        val lastDaysToGetSummary = update.text?.substringAfter("/summary ")?.toLongOrNull() ?: 5L
        return Input(LocalDate.now().minusDays(lastDaysToGetSummary))
    }

    override fun process(chatId: IdOfChat, input: Input): Output {
        return Output(
                foodTrackQueryService.listSummary(chatId, input.minDate)
        )
    }

    override fun respond(chatId: IdOfChat, output: Output, bot: Bot) {
        val response = StringBuilder("Summary \uD83D\uDCCB\n")
        output.dayToCaloriesMap.forEach { (day, calories) ->
            val dayOfMonth = day.dayOfMonth
            val month = day.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            response.append("$month $dayOfMonth: $calories\n")
        }
        bot.sendMessage(chatId.id, response.toString())
    }

    data class Input(val minDate: LocalDate)

    data class Output(val dayToCaloriesMap: SortedMap<LocalDate, Int>)
}
