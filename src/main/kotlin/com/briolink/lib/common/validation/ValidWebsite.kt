package com.briolink.lib.common.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

@ReportAsSingleViolation
@Constraint(validatedBy = [])
@Pattern(
    regexp = ValidWebsite.pattern,
    flags = [Pattern.Flag.CASE_INSENSITIVE]
)
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CLASS
)
annotation class ValidWebsite(
    val message: String = "validation.website.invalid", // "website.invalid",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
) {
    companion object {
        const val pattern = "^http(s)?://[\\w.-]+(?:\\.[\\w.-]+)+[\\w]/?$"
    }
}
