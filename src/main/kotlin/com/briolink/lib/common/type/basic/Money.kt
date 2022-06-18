package com.briolink.lib.common.type.basic

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Currency

data class Money(
    @JsonProperty
    val value: Double,
    @JsonProperty
    val currency: Currency
)

data class MoneyRange(
    @JsonProperty val start: Money? = null,
    @JsonProperty val end: Money? = null
) {
    init {
        if (start != null && end != null && end.currency.currencyCode != start.currency.currencyCode)
            throw IllegalArgumentException("Currency must be equals")
    }
}
