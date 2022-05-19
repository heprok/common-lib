package com.briolink.lib.common.type.basic

interface BaseSuggestion {
    val id: String?
    val name: String
}

data class BlSuggestion(override val id: String?, override val name: String) : BaseSuggestion
