package com.briolink.lib.common.type.basic

import org.springframework.http.HttpStatus

data class BlErrorResponse(
    val message: String? = null,
    val httpsStatus: HttpStatus
)
