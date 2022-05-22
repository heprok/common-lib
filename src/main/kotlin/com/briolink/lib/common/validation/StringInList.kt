package com.briolink.lib.common.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@MustBeDocumented
@Constraint(validatedBy = [StringInListValidator::class])
annotation class StringInList(
    val allowedValues: Array<String>,
    val message: String = "validation.string-in-list.valid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class StringInListValidator : ConstraintValidator<StringInList, String> {
    private var allowedValues: List<String> = emptyList()

    override fun initialize(constraintAnnotation: StringInList) {
        allowedValues = constraintAnnotation.allowedValues.toList()
    }

    override fun isValid(
        value: String,
        context: ConstraintValidatorContext?
    ): Boolean = allowedValues.contains(value)
}
