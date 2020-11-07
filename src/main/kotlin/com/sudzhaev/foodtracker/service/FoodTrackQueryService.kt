package com.sudzhaev.foodtracker.service

import com.sudzhaev.foodtracker.id.IdOfChat
import java.time.LocalDate
import java.util.*

interface FoodTrackQueryService {

    fun listSummary(chatId: IdOfChat, minDate: LocalDate): SortedMap<LocalDate, Int>
}
