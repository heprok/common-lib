package com.briolink.lib.common

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Locale

open class BlLocaleMessage(protected open val messageSource: MessageSource) {
    open fun getMessage(key: String, locale: Locale? = null): String = try {
        messageSource.getMessage(key, null, LocaleContextHolder.getLocale())
    } catch (e: Exception) {
        key
    }

    open fun getMessage(key: String, args: Array<Any>, locale: Locale? = null): String = try {
        messageSource.getMessage(key, args, LocaleContextHolder.getLocale())
    } catch (e: Exception) {
        key
    }
}
