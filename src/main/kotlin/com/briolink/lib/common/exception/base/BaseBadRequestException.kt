package com.briolink.lib.common.exception.base

import org.springframework.http.HttpStatus

abstract class BaseBadRequestException() : RuntimeException(), IBlException {
    override val httpsStatus = HttpStatus.BAD_REQUEST
    override val arguments: Array<String>? = null
}
