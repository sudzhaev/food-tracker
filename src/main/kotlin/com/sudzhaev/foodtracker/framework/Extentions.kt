package com.sudzhaev.foodtracker.framework

import com.github.kotlintelegrambot.entities.Update
import com.mapk.krowmapper.KRowMapper
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

inline fun <reified T> ApplicationContext.listBeans(): List<T> {
    return getBeansOfType(T::class.java).values.toList()
}

inline fun <reified T : Any> NamedParameterJdbcTemplate.select(sql: String, params: Map<String, Any>): List<T> {
    return query(sql, params, KRowMapper(T::class))
}

val Update.text: String?
    get() {
        val text = this.message?.text?.trim() ?: return null
        return if (text.isNotBlank()) text else null
    }
