package com.briolink.lib.common.utils

import org.joda.time.DateTime
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

object RandomUtils {
    fun randomDate(startYear: Int, endYear: Int, minDate: LocalDate? = null): LocalDate {
        val day: Int = Random.nextInt(minDate?.dayOfMonth ?: 1, 28)
        val month: Int = Random.nextInt(minDate?.month?.value ?: 1, 12)
        val year: Int = Random.nextInt(minDate?.year ?: startYear, endYear)
        return LocalDate.of(year, month, day)
    }

    fun randomInstant(startYear: Int, endYear: Int): Instant {
        val date = randomDate(startYear, endYear)
        val datetime = DateTime(
            date.year,
            date.month.value,
            date.dayOfMonth,
            Random.nextInt(0, 23),
            Random.nextInt(0, 59),
        )
        return Instant.ofEpochMilli(datetime.millis)
    }

    fun randomIds(list: List<UUID>, count: Int, excludeIds: List<UUID>? = null): List<UUID> {
        val result = mutableListOf<UUID>()

        val ids = list.filter { !(excludeIds?.contains(it) ?: false) }
        return ids.subList(0, count)
    }
}
