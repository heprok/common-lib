package com.briolink.lib.common.exception.base

import org.springframework.http.HttpStatus

abstract class BaseAccessDeniedException() : RuntimeException(), IBlException {
    override val httpsStatus = HttpStatus.FORBIDDEN
    override val arguments: Array<String>? = null
}
