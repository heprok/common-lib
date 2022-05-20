package com.briolink.lib.common.exception

import com.briolink.lib.common.exception.base.BaseUnavailableException

class UnavailableException(override val serviceName: String?) : BaseUnavailableException(serviceName) {
    override val code: String = "exception.service.unavailable"
}
