package com.briolink.lib.common.util

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Locale

open class BlLocaleMessage(protected val messageSource: MessageSource) {
    open fun getMessage(key: String, locale: Locale? = null): String = try {
        messageSource.getMessage(key, null, LocaleContextHolder.getLocale())
    } catch (e: Exception) {
        key
    }
}
