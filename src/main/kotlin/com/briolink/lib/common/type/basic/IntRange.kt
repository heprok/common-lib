package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty

data class IntRange(
    @JsonProperty val start: Int? = null,
    @JsonProperty val end: Int? = null
) {
    constructor(start: Double? = null, end: Double? = null) : this(start?.toInt(), end?.toInt())
    constructor(start: Float? = null, end: Float? = null) : this(start?.toInt(), end?.toInt())
}
