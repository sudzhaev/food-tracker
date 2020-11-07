package com.sudzhaev.foodtracker.dto

import java.time.LocalDateTime

data class FoodTrack(
        val name: String,
        val calories: Int,
        val createdAt: LocalDateTime,
)
