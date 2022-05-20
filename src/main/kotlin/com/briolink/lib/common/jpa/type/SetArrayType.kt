package com.briolink.lib.common.jpa.type

import com.vladmihalcea.hibernate.type.array.internal.AbstractArrayType
import com.vladmihalcea.hibernate.type.array.internal.AbstractArrayTypeDescriptor
import com.vladmihalcea.hibernate.type.array.internal.ArrayUtil
import com.vladmihalcea.hibernate.util.ReflectionUtils
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.MutableMutabilityPlan
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.WildcardType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Collections
import java.util.Date
import java.util.Properties
import java.util.UUID

class SetArrayType : AbstractArrayType<Any>(SetArrayTypeDescriptor()) {
    override fun getName(): String = NAME
    override fun setParameterValues(parameters: Properties) {
        (javaTypeDescriptor as SetArrayTypeDescriptor).setParameterValues(parameters)
    }
    companion object {
        const val NAME = "set-array"
    }
}

class SetArrayTypeDescriptor : AbstractArrayTypeDescriptor<Any>(Any::class.java, SetArrayMutabilityPlan()) {
    private var sqlArrayType: String? = null
    override fun getSqlArrayType(): String = sqlArrayType!!

    override fun <X : Any?> unwrap(value: Any?, type: Class<X>?, options: WrapperOptions?): X? {
        if (value == null) {
            return null
        }

        return if (value is Set<*>) {
            super.unwrap(value.toTypedArray(), type, options)
        } else {
            throw UnsupportedOperationException("The provided $value is not a Object[] or Set!")
        }
    }

    override fun <X : Any?> wrap(value: X, options: WrapperOptions?): Any? {
        val wrappedObject = super.wrap(value, options)
        var list: MutableSet<*>? = null
        if (wrappedObject != null) {
            list = mutableSetOf<Any?>()
            if (wrappedObject is Array<*>) {
                Collections.addAll(list, *wrappedObject)
            } else {
                throw UnsupportedOperationException("The wrapped object $value is not an Object[]!")
            }
        }

        return list
    }

    override fun areEqual(one: Any, another: Any): Boolean {
        if (one === another) return true
        if (one is Set<*> && another is Set<*>) return ArrayUtil.isEquals(one.toTypedArray(), another.toTypedArray())
        if (one is Array<*> && another is Array<*>) return ArrayUtil.isEquals(one, another)

        throw UnsupportedOperationException("The provided $one and $another are not Object[] or Set!")
    }

    override fun setParameterValues(parameters: Properties) {
        val entityClass: Class<*> = ReflectionUtils.getClass<Any>(parameters.getProperty(ENTITY))
        val property = parameters.getProperty(PROPERTY)
        val memberGenericType = ReflectionUtils.getMemberGenericTypeOrNull(entityClass, property)
        if (memberGenericType is ParameterizedType) {
            var genericType = memberGenericType.actualTypeArguments[0]
            if (genericType is WildcardType) {
                genericType = genericType.upperBounds[0]
            }
            val arrayElementClass: Class<Any> = ReflectionUtils.getClass(genericType.typeName)
            arrayObjectClass =
                (if (arrayElementClass.isArray) arrayElementClass else ArrayUtil.toArrayClass(arrayElementClass)) as Class<Any>?
            sqlArrayType = parameters.getProperty(AbstractArrayType.SQL_ARRAY_TYPE)
            if (sqlArrayType == null) {
                sqlArrayType = if (Int::class.java.isAssignableFrom(arrayElementClass)) {
                    "integer"
                } else if (Long::class.java.isAssignableFrom(arrayElementClass)) {
                    "bigint"
                } else if (Double::class.java.isAssignableFrom(arrayElementClass)) {
                    "float8"
                } else if (String::class.java.isAssignableFrom(arrayElementClass)) {
                    "text"
                } else if (UUID::class.java.isAssignableFrom(arrayElementClass)) {
                    "uuid"
                } else if (
                    Date::class.java.isAssignableFrom(arrayElementClass) || LocalDateTime::class.java.isAssignableFrom(arrayElementClass)
                ) {
                    "timestamp"
                } else if (Boolean::class.java.isAssignableFrom(arrayElementClass)) {
                    "boolean"
                } else if (BigDecimal::class.java.isAssignableFrom(arrayElementClass)) {
                    "decimal"
                } else if (LocalDate::class.java.isAssignableFrom(arrayElementClass)) {
                    "date"
                } else {
                    throw UnsupportedOperationException("The $arrayElementClass is not supported yet!")
                }
            }
        } else {
            throw UnsupportedOperationException("The property $property in the $entityClass entity is not parameterized!")
        }
    }
}

private class SetArrayMutabilityPlan : MutableMutabilityPlan<Any>() {
    override fun deepCopyNotNull(value: Any): Any {
        return if (value is Set<*>) {
            value.toMutableSet()
        } else if (value.javaClass.isArray) {
            val array = value as Array<*>
            ArrayUtil.deepCopy<Any>(array)
        } else {
            throw UnsupportedOperationException("The provided $value is not a Set!")
        }
    }

    override fun assemble(cached: Serializable): Any {
        if (cached.javaClass.isArray) {
            val array = cached as Array<*>
            return setOf(*array)
        }
        return super.assemble(cached)
    }
}
