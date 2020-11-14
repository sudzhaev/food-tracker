package com.sudzhaev.foodtracker.service

import java.time.LocalDate

interface TimeProvider {

    fun getToday(): LocalDate
}
