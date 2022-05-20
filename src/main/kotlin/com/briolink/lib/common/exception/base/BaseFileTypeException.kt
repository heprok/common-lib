package com.briolink.lib.common.exception.base

import org.springframework.http.HttpStatus

abstract class BaseFileTypeException() : RuntimeException(), IBlException {
    override val message: String = "Invalid file type: allowed JPEG or PNG images."
    override val httpsStatus = HttpStatus.BAD_REQUEST
    override val arguments: Array<String>? = null
}
