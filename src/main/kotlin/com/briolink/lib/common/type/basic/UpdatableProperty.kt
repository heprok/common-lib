package com.briolink.lib.common.type.basic

sealed class UpdatableProperty<out T>

data class Update<T>(val value: T) : UpdatableProperty<T>()
