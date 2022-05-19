package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseNotFoundException(override val message: String = "Entity not found") :
    RuntimeException(),
    ExceptionInterface {
    override val httpsStatus = HttpStatus.NOT_FOUND
}
