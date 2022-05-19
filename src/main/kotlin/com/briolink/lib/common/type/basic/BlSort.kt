package com.briolink.lib.common.type.basic

enum class BlSortDirectionEnum {
    ASC,
    DESC
}

data class BlSortParameter<T>(
    val key: T,
    val direction: BlSortDirectionEnum,
)
