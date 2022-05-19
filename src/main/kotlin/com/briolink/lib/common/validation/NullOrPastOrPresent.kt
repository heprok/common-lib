package com.briolink.lib.common.validation

import org.hibernate.validator.constraints.CompositionType
import org.hibernate.validator.constraints.ConstraintComposition
import javax.validation.Constraint
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Null
import javax.validation.constraints.PastOrPresent
import kotlin.reflect.KClass

@ConstraintComposition(CompositionType.OR)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Null
@PastOrPresent
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NullOrPastOrPresent(
    val message: String = "{javax.validation.constraints.PastOrPresent.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)
