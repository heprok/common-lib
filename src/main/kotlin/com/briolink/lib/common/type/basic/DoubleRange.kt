package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty

data class DoubleRange(
    @JsonProperty val start: Double? = null,
    @JsonProperty val end: Double? = null
) {
    constructor(start: Float? = null, end: Float? = null) : this(start?.toDouble(), end?.toDouble())
}
