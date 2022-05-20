package com.briolink.lib.common.exception

import com.briolink.lib.common.exception.base.BaseValidationException

class ValidationException(override val message: String = "Validation error") : BaseValidationException() {
    override val code: String = "exception.validation.error"
}
