package com.sudzhaev.foodtracker.service

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class TimeProviderImpl : TimeProvider {

    override fun getToday(): LocalDate {
        return LocalDate.now()
    }
}
