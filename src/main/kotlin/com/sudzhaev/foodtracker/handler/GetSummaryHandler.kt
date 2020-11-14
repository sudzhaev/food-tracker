package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ParseMode
import com.sudzhaev.foodtracker.framework.asReadableString
import com.sudzhaev.foodtracker.framework.sendMessage
import com.sudzhaev.foodtracker.handler.common.DateBasedCommandHandler
import com.sudzhaev.foodtracker.handler.common.DateBasedInput
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.service.FoodTrackQueryService
import com.sudzhaev.foodtracker.service.TimeProvider
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class GetSummaryHandler(
        private val foodTrackQueryService: FoodTrackQueryService,
        private val timeProvider: TimeProvider,
) : DateBasedCommandHandler<GetSummaryHandler.Output>("summary") {

    override fun processLastNDaysInput(chatId: IdOfChat, input: DateBasedInput.LastNDaysInput): Output {
        val minDate = timeProvider.getToday().minusDays(input.lastNDays)
        return Output(foodTrackQueryService.listSummaryForLastNDays(chatId, minDate))
    }

    override fun processDateInput(chatId: IdOfChat, input: DateBasedInput.DateInput): Output {
        return Output(foodTrackQueryService.listSummaryForDate(chatId, input.date))
    }

    override fun respond(chatId: IdOfChat, output: Output, bot: Bot) {
        val response = StringBuilder("<b>Summary</b> 📈\n")
        output.dayToCaloriesMap.forEach { (date, calories) ->
            response.append("${date.asReadableString()}: $calories\n")
        }
        bot.sendMessage(chatId, response.toString(), parseMode = ParseMode.HTML)
    }

    data class Output(val dayToCaloriesMap: SortedMap<LocalDate, Int>)
}
