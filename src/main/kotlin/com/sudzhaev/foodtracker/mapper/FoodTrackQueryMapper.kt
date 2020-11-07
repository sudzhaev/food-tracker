package com.sudzhaev.foodtracker.mapper

import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.framework.select
import com.sudzhaev.foodtracker.id.IdOfChat
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class FoodTrackQueryMapper(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun listFoodTracks(chatId: IdOfChat, minDate: LocalDate): List<FoodTrack> {
        return jdbcTemplate.select("""
            SELECT name,
                   calories,
                   created_at createdAt
            FROM food_track
            WHERE chat_id = :chatId AND created_at > :minDate
        """, mapOf("chatId" to chatId.id, "minDate" to minDate.atStartOfDay()))
    }
}
