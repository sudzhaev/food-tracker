package com.sudzhaev.foodtracker.framework

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.network.Response
import com.mapk.krowmapper.KRowMapper
import com.sudzhaev.foodtracker.id.IdOfChat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.*

inline fun <reified T> logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)!!
}

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

fun Update.commandArgs(command: String): String? {
    val message = text ?: return null
    val args = message.substringAfter("/$command ")
    return if (args != message) args else null
}

fun Bot.sendMessage(
        chatId: IdOfChat,
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
        replyMarkup: ReplyMarkup? = null
): Pair<retrofit2.Response<Response<Message>?>?, Exception?> {
    return sendMessage(
            chatId = chatId.id,
            text = text,
            parseMode = parseMode,
            disableWebPagePreview = disableWebPagePreview,
            disableNotification = disableNotification,
            replyToMessageId = replyToMessageId,
            replyMarkup = replyMarkup
    )
}

fun String.toLocalDateOrNull(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE): LocalDate? {
    return try {
        LocalDate.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}

fun LocalDate.asReadableString(): String {
    val month = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    return "$month ${this.dayOfMonth}"
}
