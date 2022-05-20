package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseExistsException() : RuntimeException(), IBlException {
    override val message: String = "Entity exist"
    override val httpsStatus = HttpStatus.CONFLICT
}
