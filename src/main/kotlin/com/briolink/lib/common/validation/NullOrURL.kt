package com.briolink.lib.common.validation

import org.hibernate.validator.constraints.CompositionType
import org.hibernate.validator.constraints.ConstraintComposition
import org.hibernate.validator.constraints.URL
import javax.validation.Constraint
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Null
import kotlin.reflect.KClass

@ConstraintComposition(CompositionType.OR)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Null
@URL
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
annotation class NullOrURL(
    val message: String = "validation.null-or-valid-url", // "Must be null or valid URL",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)
