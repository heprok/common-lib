package com.briolink.lib.common.utils

import org.springframework.boot.CommandLineRunner
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

abstract class BlDataLoader : CommandLineRunner {

    @Throws(Exception::class)
    override fun run(vararg args: String?) {
        if (System.getenv("load_data") == "true") loadData()
    }

    abstract fun loadData()

    open fun randomDate(startYear: Int, endYear: Int, minDate: LocalDate? = null): LocalDate {
        return RandomUtils.randomDate(startYear, endYear, minDate)
    }

    open fun randomInstant(startYear: Int, endYear: Int): Instant {
        return RandomUtils.randomInstant(startYear, endYear)
    }

    open fun randomIds(list: List<UUID>, count: Int, excludeIds: List<UUID>? = null): List<UUID> {
        return RandomUtils.randomIds(list, count, excludeIds)
    }
}
