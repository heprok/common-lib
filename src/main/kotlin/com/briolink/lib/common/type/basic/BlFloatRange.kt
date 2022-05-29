package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty

data class BlFloatRange(
    @JsonProperty val start: Float? = null,
    @JsonProperty val end: Float? = null
)
