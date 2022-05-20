package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseNotFoundException() : RuntimeException(), IBlException {
    override val message: String = "Entity not found"
    override val httpsStatus = HttpStatus.NOT_FOUND
}
