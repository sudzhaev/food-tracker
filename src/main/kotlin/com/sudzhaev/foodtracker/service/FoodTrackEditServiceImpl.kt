package com.sudzhaev.foodtracker.service

import com.sudzhaev.foodtracker.dto.FoodTrack
import com.sudzhaev.foodtracker.id.IdOfChat
import com.sudzhaev.foodtracker.mapper.FoodTrackEditMapper
import org.springframework.stereotype.Component

@Component
class FoodTrackEditServiceImpl(private val foodTrackEditMapper: FoodTrackEditMapper) : FoodTrackEditService {

    override fun save(chatId: IdOfChat, foodTrack: FoodTrack) {
        foodTrackEditMapper.saveFoodTrack(chatId, foodTrack)
    }
}
