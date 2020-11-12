package com.sudzhaev.foodtracker.service

import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.dto.FoodTrackMin
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.mapper.FoodTrackQueryMapper
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import kotlin.Comparator

@Component
class FoodTrackQueryServiceImpl(private val foodTrackQueryMapper: FoodTrackQueryMapper) : FoodTrackQueryService {

    override fun listSummaryForLastNDays(chatId: IdOfChat, minDate: LocalDate): SortedMap<LocalDate, Int> {
        return foodTrackQueryMapper.listFoodTracksByMinDate(chatId, minDate)
                .groupBy { it.createdAt.toLocalDate() }
                .mapValues { it.value.sumBy(FoodTrack::calories) }
                .toSortedMap(Comparator.reverseOrder())
    }

    override fun listStatisticsForLastNDays(chatId: IdOfChat, minDate: LocalDate): SortedMap<LocalDate, SortedSet<FoodTrackMin>> {
        return buildStatistics(
                foodTrackQueryMapper.listFoodTracksByMinDate(chatId, minDate)
        )
    }

    override fun listStatisticsForDate(chatId: IdOfChat, date: LocalDate): SortedMap<LocalDate, SortedSet<FoodTrackMin>> {
        return buildStatistics(
                foodTrackQueryMapper.listFoodTracksByDate(chatId, date)
        )
    }

    private fun buildStatistics(foodTracks: List<FoodTrack>): SortedMap<LocalDate, SortedSet<FoodTrackMin>> {
        return foodTracks
                .groupBy { it.createdAt.toLocalDate() }
                .mapValues {
                    it.value.groupBy(FoodTrack::name)
                            .mapValues { (name, foodTracks) -> FoodTrackMin(name, foodTracks.sumBy(FoodTrack::calories)) }
                            .values
                            .toSortedSet(Comparator.comparing(FoodTrackMin::calories).reversed())
                }
                .toSortedMap(Comparator.reverseOrder())
    }
}
