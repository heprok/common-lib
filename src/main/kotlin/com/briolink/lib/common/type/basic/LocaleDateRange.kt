package com.briolink.lib.common.type.basic

import com.briolink.lib.common.type.interfaces.IBaseLocalDateRange
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.ZoneOffset

data class LocaleDateRange(
    @JsonProperty override val startDate: LocalDate? = null,
    @JsonProperty override val endDate: LocalDate? = null
) : IBaseLocalDateRange {
    val startYear: Year?
        get() = startDate?.let { Year.of(it.year) }

    val endYear: Year?
        get() = endDate?.let { Year.of(it.year) }

    val endDateInstant: Instant?
        get() = endDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()

    val startDateInstant: Instant?
        get() = startDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
}
