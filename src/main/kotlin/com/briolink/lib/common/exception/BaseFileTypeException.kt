package com.briolink.lib.common.exception

import org.springframework.http.HttpStatus

abstract class BaseFileTypeException(override val message: String = "Invalid file type: allowed JPEG or PNG images.") :
    RuntimeException(),
    ExceptionInterface {
    override val httpsStatus = HttpStatus.BAD_REQUEST
}
