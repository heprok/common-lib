package com.briolink.lib.common.exception.base

import org.springframework.http.HttpStatus

abstract class BaseNotFoundException() : RuntimeException(), IBlException {
    override val httpsStatus = HttpStatus.NOT_FOUND
    override val arguments: Array<String>? = null
}
