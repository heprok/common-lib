package com.briolink.lib.common.exception.base

import org.springframework.http.HttpStatus

abstract class BaseValidationException() : IBlException, RuntimeException() {
    override val httpsStatus: HttpStatus = HttpStatus.BAD_REQUEST
    override val arguments: Array<String>? = null
}
