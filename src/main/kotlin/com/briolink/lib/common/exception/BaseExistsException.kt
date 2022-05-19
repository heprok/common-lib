package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseExistsException(override val message: String = "Entity exist") :
    RuntimeException(),
    ExceptionInterface {
    override val httpsStatus = HttpStatus.CONFLICT
}
