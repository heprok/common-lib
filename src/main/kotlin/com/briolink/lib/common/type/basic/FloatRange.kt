package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty

data class FloatRange(
    @JsonProperty val start: Float? = null,
    @JsonProperty val end: Float? = null
) {
    constructor(start: Double? = null, end: Double? = null) : this(start?.toFloat(), end?.toFloat())
}
