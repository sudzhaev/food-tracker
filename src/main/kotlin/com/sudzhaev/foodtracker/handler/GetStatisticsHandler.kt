package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ParseMode
import com.sudzhaev.foodtracker.dto.FoodTrackMin
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
class GetStatisticsHandler(
        private val foodTrackQueryService: FoodTrackQueryService,
        private val timeProvider: TimeProvider,
) :
        DateBasedCommandHandler<GetStatisticsHandler.Output>("stats") {

    override fun processLastNDaysInput(chatId: IdOfChat, input: DateBasedInput.LastNDaysInput): Output {
        val minDate = timeProvider.getToday().minusDays(input.lastNDays)
        return Output(foodTrackQueryService.listStatisticsForLastNDays(chatId, minDate))
    }

    override fun processDateInput(chatId: IdOfChat, input: DateBasedInput.DateInput): Output {
        return Output(foodTrackQueryService.listStatisticsForDate(chatId, input.date))
    }

    override fun respond(chatId: IdOfChat, output: Output, bot: Bot) {
        val response = StringBuilder("<b>Statistics</b> 📝")
        output.statistics.forEach { (date, foodTracks) ->
            response.append("\n")
                    .append(date.asReadableString())
                    .append("\n")
                    .append(foodTracks.joinToString("\n") { "  ${it.name}: ${it.calories}" })
        }
        bot.sendMessage(chatId, response.toString(), parseMode = ParseMode.HTML)
    }

    data class Output(val statistics: SortedMap<LocalDate, SortedSet<FoodTrackMin>>)
}
