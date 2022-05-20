package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseUnavailableException(open val serviceName: String?) : IBlException, RuntimeException() {
    override val message: String
        get() = if (serviceName != null) "$serviceName: Service unavailable" else "Service unavailable"

    override val httpsStatus = HttpStatus.SERVICE_UNAVAILABLE
}
