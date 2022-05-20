package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseAccessDeniedException() : RuntimeException(), IBlException {
    override val httpsStatus = HttpStatus.FORBIDDEN
}
