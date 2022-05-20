package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseValidationException() : IBlException, RuntimeException() {
    override val message: String = "Validation error"
    override val httpsStatus: HttpStatus = HttpStatus.BAD_REQUEST
}
