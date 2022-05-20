package com.briolink.lib.common.exception.base

import org.springframework.http.HttpStatus

interface IBlException {
    /**
     * Code i18n in resource messages
     */
    val code: String

    /**
     * List arguments in i18n code
     */
    val arguments: Array<String>?
    val httpsStatus: HttpStatus
}
