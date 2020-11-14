package com.sudzhaev.foodtracker.service

import com.sudzhaev.foodtracker.dto.FoodTrackMin
import com.sudzhaev.foodtracker.id.IdOfChat
import java.time.LocalDate
import java.util.*

interface FoodTrackQueryService {

    fun listSummaryForLastNDays(chatId: IdOfChat, minDate: LocalDate): SortedMap<LocalDate, Int>

    fun listSummaryForDate(chatId: IdOfChat, date: LocalDate): SortedMap<LocalDate, Int>

    fun listStatisticsForLastNDays(chatId: IdOfChat, minDate: LocalDate): SortedMap<LocalDate, SortedSet<FoodTrackMin>>

    fun listStatisticsForDate(chatId: IdOfChat, date: LocalDate): SortedMap<LocalDate, SortedSet<FoodTrackMin>>
}
