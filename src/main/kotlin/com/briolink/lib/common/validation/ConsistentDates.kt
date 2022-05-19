package com.briolink.lib.common.validation

import com.briolink.lib.common.type.interfaces.IBaseLocalDateRange
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.CLASS)
@MustBeDocumented
@Constraint(validatedBy = [ConsistentLocalDatesValidator::class])
annotation class ConsistentLocalDates(
    val message: String = "validation.range-date.start-before-end", // "start date must be before end date"s
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ConsistentLocalDatesValidator : ConstraintValidator<ConsistentLocalDates, IBaseLocalDateRange> {
    override fun isValid(
        dateRange: IBaseLocalDateRange,
        context: ConstraintValidatorContext?
    ) =
        when {
            dateRange.startDate == null && dateRange.endDate == null -> true
            dateRange.startDate != null && dateRange.endDate == null -> true
            dateRange.startDate != null && dateRange.endDate != null && (
                dateRange.startDate!!.isBefore(dateRange.endDate) || dateRange.startDate!!.isEqual(dateRange.endDate)
                ) -> true
            else -> false
        }
}
