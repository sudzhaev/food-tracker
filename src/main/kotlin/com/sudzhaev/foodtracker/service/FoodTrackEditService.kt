package com.sudzhaev.foodtracker.service

import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.id.IdOfChat

interface FoodTrackEditService {

    fun save(chatId: IdOfChat, foodTrack: FoodTrack)
}
