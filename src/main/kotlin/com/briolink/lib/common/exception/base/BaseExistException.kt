package com.briolink.lib.common.exception.base

import org.springframework.http.HttpStatus

abstract class BaseExistException() : RuntimeException(), IBlException {
    override val httpsStatus = HttpStatus.CONFLICT
    override val arguments: Array<String>? = null
}
