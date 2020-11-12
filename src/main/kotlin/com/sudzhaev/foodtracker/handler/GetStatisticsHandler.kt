package com.sudzhaev.foodtracker.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.dto.FoodTrackMin
import com.sudzhaev.foodtracker.framework.*
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.service.FoodTrackQueryService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class GetStatisticsHandler(private val foodTrackQueryService: FoodTrackQueryService) :
        MessageHandler<GetStatisticsHandler.Input, GetStatisticsHandler.Output>(CommandRequest("stats")) {

    override fun parseInput(chatId: IdOfChat, update: Update): Input {
        val commandArgs = update.commandArgs("stats") ?: return Input.StatsForLastNDays(5)
        val lastNDays = commandArgs.toLongOrNull()
        if (lastNDays != null) {
            return Input.StatsForLastNDays(lastNDays)
        }
        val date = commandArgs.toLocalDateOrNull()
        if (date != null) {
            return Input.StatsForDate(date)
        }
        raise(PARSE_ERROR)
    }

    override fun process(chatId: IdOfChat, input: Input): Output {
        return Output(when (input) {
            is Input.StatsForLastNDays -> {
                val minDate = LocalDate.now().minusDays(input.lastNDays)
                foodTrackQueryService.listStatisticsForLastNDays(chatId, minDate)
            }
            is Input.StatsForDate -> {
                foodTrackQueryService.listStatisticsForDate(chatId, input.date)
            }
        })
    }

    override fun respond(chatId: IdOfChat, output: Output, bot: Bot) {
        val response = StringBuilder("<b>Statistics</b> \uD83D\uDCC4")
        output.statistics.forEach { (date, foodTracks) ->
            response.append("\n")
                    .append(date.asReadableString())
                    .append("\n")
                    .append(foodTracks.joinToString("\n") { "  ${it.name}: ${it.calories}" })
        }
        bot.sendMessage(chatId, response.toString(), parseMode = ParseMode.HTML)
    }

    sealed class Input {
        data class StatsForLastNDays(val lastNDays: Long) : Input()
        data class StatsForDate(val date: LocalDate) : Input()
    }

    data class Output(val statistics: SortedMap<LocalDate, SortedSet<FoodTrackMin>>)
}
