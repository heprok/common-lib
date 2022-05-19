package com.briolink.lib.common.validation

import com.briolink.lib.common.type.interfaces.IBaseYearRange
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.CLASS)
@MustBeDocumented
@Constraint(validatedBy = [ConsistentYearsValidator::class])
annotation class ConsistentYears(
    val message: String = "validation.range-year.start-before-end", // "start year must be before end year"
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ConsistentYearsValidator : ConstraintValidator<ConsistentYears, IBaseYearRange> {
    override fun isValid(
        yearRange: IBaseYearRange,
        context: ConstraintValidatorContext?
    ): Boolean = when {
        yearRange.startYear == null && yearRange.endYear == null -> true
        yearRange.startYear != null && yearRange.endYear != null && yearRange.startYear!!.isBefore(yearRange.endYear) -> true
        else -> false
    }
}
