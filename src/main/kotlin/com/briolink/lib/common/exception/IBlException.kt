package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

interface IBlException {
    val code: String // i18n code
    val httpsStatus: HttpStatus
}
