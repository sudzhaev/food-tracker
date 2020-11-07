package com.sudzhaev.foodtracker.service

import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.mapper.FoodTrackQueryMapper
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class FoodTrackQueryServiceImpl(private val foodTrackQueryMapper: FoodTrackQueryMapper) : FoodTrackQueryService {

    override fun listSummary(chatId: IdOfChat, minDate: LocalDate): SortedMap<LocalDate, Int> {
        return foodTrackQueryMapper.listFoodTracks(chatId, minDate)
                .groupBy { it.createdAt.toLocalDate() }
                .mapValues { it.value.sumBy(FoodTrack::calories) }
                .toSortedMap(Comparator.reverseOrder())
    }
}
