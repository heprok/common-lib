package com.briolink.lib.common.exception

import com.briolink.lib.common.exception.base.BaseBadRequestException

class BadRequestException(override val message: String = "Bad request") : BaseBadRequestException() {
    override val code: String = "exception.bad-request"
}
