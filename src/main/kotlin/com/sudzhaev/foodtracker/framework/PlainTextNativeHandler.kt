package com.sudzhaev.foodtracker.framework

import com.github.kotlintelegrambot.HandleUpdate
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.entities.Update

/**
 * Handles only plain text, not commands
 */
class PlainTextNativeHandler(handler: HandleUpdate) : Handler(handler) {

    override val groupIdentifier: String = "PlainTextHandler"

    override fun checkUpdate(update: Update): Boolean {
        val text = update.text
        return text != null && !text.startsWith("/")
    }
}
