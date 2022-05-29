package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

data class BlErrorResponse(
    @JsonProperty
    val message: String? = null,
    @JsonProperty
    val httpsStatus: HttpStatus
)
