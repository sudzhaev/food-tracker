package com.sudzhaev.foodtracker.handler.common

import com.github.kotlintelegrambot.entities.Update
import com.sudzhaev.foodtracker.framework.*
import com.sudzhaev.foodtracker.id.IdOfChat
import java.time.LocalDate

sealed class DateBasedInput {
    data class LastNDaysInput(val lastNDays: Long) : DateBasedInput()
    data class DateInput(val date: LocalDate) : DateBasedInput()
}

abstract class DateBasedCommandHandler<OUTPUT>(private val command: String) : MessageHandler<DateBasedInput, OUTPUT>(CommandRequest(command)) {

    final override fun parseInput(chatId: IdOfChat, update: Update): DateBasedInput {
        val commandArgs = update.commandArgs(command) ?: return DateBasedInput.LastNDaysInput(5)
        val lastNDays = commandArgs.toLongOrNull()
        if (lastNDays != null) {
            validate(lastNDays > 0) { "$PARSE_ERROR. Days must be positive" }
            return DateBasedInput.LastNDaysInput(lastNDays)
        }
        val date = commandArgs.toLocalDateOrNull()
        if (date != null) {
            val today = LocalDate.now()
            validate(date.isBefore(today) || date.isEqual(today)) { "$PARSE_ERROR. Date must be in past" }
            return DateBasedInput.DateInput(date)
        }
        raise(PARSE_ERROR)
    }

    final override fun process(chatId: IdOfChat, input: DateBasedInput): OUTPUT {
        return when (input) {
            is DateBasedInput.LastNDaysInput -> processLastNDaysInput(chatId, input)
            is DateBasedInput.DateInput -> processDateInput(chatId, input)
        }
    }

    protected abstract fun processLastNDaysInput(chatId: IdOfChat, input: DateBasedInput.LastNDaysInput): OUTPUT

    protected abstract fun processDateInput(chatId: IdOfChat, input: DateBasedInput.DateInput): OUTPUT
}
