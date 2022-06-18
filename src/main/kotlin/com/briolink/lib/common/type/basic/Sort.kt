package com.briolink.lib.common.type.basic

enum class SortDirectionEnum {
    ASC,
    DESC
}

data class SortParameter<T>(
    val key: T,
    val direction: SortDirectionEnum,
)
