package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty

data class BlIntRange(
    @JsonProperty val start: Int? = null,
    @JsonProperty val end: Int? = null
)
