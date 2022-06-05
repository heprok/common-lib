package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty

data class BlDoubleRange(
    @JsonProperty val start: Double? = null,
    @JsonProperty val end: Double? = null
)
