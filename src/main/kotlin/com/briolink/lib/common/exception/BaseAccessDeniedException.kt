package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseAccessDeniedException() : RuntimeException(), ExceptionInterface {
    override val httpsStatus = HttpStatus.FORBIDDEN
}
