package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty

data class ValueWithCount<V, C : Number?>(
    @JsonProperty
    val value: V,
    @JsonProperty
    val count: C
)
