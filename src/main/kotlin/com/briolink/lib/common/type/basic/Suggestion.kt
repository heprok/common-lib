package com.briolink.lib.common.type.basic

import com.briolink.lib.common.type.interfaces.IBaseSuggestion
import com.fasterxml.jackson.annotation.JsonProperty

data class Suggestion(
    @JsonProperty override val id: String?,
    @JsonProperty override val name: String
) : IBaseSuggestion

data class ListSuggestion(
    @JsonProperty val listSuggestion: List<Suggestion>
)
