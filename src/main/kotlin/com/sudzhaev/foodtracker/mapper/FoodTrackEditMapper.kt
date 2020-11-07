package com.sudzhaev.foodtracker.mapper

import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.id.IdOfChat
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class FoodTrackEditMapper(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun saveFoodTrack(chatId: IdOfChat, foodTrack: FoodTrack) {
        val inserted = jdbcTemplate.update("""
            INSERT INTO food_track (chat_id, name, calories, created_at)
            VALUES (:chatId, :name, :calories, :createdAt)
        """, mapOf(
                "chatId" to chatId.id,
                "name" to foodTrack.name,
                "calories" to foodTrack.calories,
                "createdAt" to foodTrack.createdAt
        ))
        check(inserted == 1) { "foodTrack not inserted: chat = ${chatId.id}, foodTrack = $foodTrack" }
    }
}
