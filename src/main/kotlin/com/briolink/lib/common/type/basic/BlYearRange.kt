package com.briolink.lib.common.type.basic

import com.briolink.lib.common.type.interfaces.IBaseYearRange
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId

data class BlYearRange(
    @JsonProperty override val startYear: Year? = null,
    @JsonProperty override val endYear: Year? = null
) : IBaseYearRange {
    val startDate: LocalDate?
        get() = startYear?.let { LocalDate.of(it.value, 1, 1) }

    val endDate: LocalDate?
        get() = endYear?.let { LocalDate.of(it.value, 12, 31) }

    val endDateInstant: Instant?
        get() = endDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()

    val startDateInstant: Instant?
        get() = startDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
}
