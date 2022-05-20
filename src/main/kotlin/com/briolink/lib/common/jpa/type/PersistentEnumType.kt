package com.briolink.lib.common.jpa.type

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.DynamicParameterizedType
import org.hibernate.usertype.DynamicParameterizedType.ParameterType
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types
import java.util.Arrays
import java.util.Objects
import java.util.Properties
import java.util.stream.Collectors

interface PersistentEnum {
    val value: String
}

class PersistentEnumType : UserType, DynamicParameterizedType {
    private var enumClass: Class<*>? = null

    override fun setParameterValues(parameters: Properties) {
        val reader = parameters[DynamicParameterizedType.PARAMETER_TYPE] as ParameterType
        enumClass = reader.returnedClass.asSubclass<Enum<*>>(Enum::class.java)
    }

    override fun sqlTypes(): IntArray = SQL_TYPES

    override fun returnedClass(): Class<*>? = enumClass

    @Throws(HibernateException::class)
    override fun equals(x: Any?, y: Any?): Boolean =
        if (x == null && y == null) {
            true
        } else if (x == null || y == null) {
            false
        } else x == y

    @Throws(HibernateException::class)
    override fun hashCode(x: Any?): Int = x?.hashCode() ?: 0

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeGet(rs: ResultSet, names: Array<String>, session: SharedSessionContractImplementor, owner: Any): Any? {
        val value = rs.getString(names[0]) ?: return null
        for (`object` in enumClass!!.enumConstants) {
            if (value == (`object` as PersistentEnum).value) {
                return `object`
            }
        }
        throw RuntimeException(String.format("Unknown name value [%s] for enum class [%s]", value, enumClass!!.name))
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(st: PreparedStatement, value: Any?, index: Int, session: SharedSessionContractImplementor) {
        when (value) {
            null -> st.setNull(index, SQL_TYPES[0])
            else -> st.setObject(index, (value as PersistentEnum).value, Types.OTHER)
        }
    }

    @Throws(HibernateException::class)
    override fun deepCopy(value: Any?): Any? = value

    override fun isMutable(): Boolean = false

    @Throws(HibernateException::class)
    override fun disassemble(value: Any): Serializable = value as Serializable

    @Throws(HibernateException::class)
    override fun assemble(cached: Serializable, owner: Any): Any = cached

    @Throws(HibernateException::class)
    override fun replace(original: Any?, target: Any?, owner: Any?): Any? = original

    companion object {
        private val SQL_TYPES = intArrayOf(Types.OTHER)
    }
}

class PersistentEnumSetType<T : Enum<T>> : UserType, DynamicParameterizedType {

    private var fieldType: Class<*>? = null

    override fun setParameterValues(parameters: Properties) {
        val entityClass = parameters[DynamicParameterizedType.ENTITY] as String
        val fieldName = parameters[DynamicParameterizedType.PROPERTY] as String

        fieldType = try {
            val genericType: ParameterizedType = Class.forName(entityClass).getDeclaredField(fieldName).genericType as ParameterizedType
            genericType.actualTypeArguments[0] as Class<*>
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException(e)
        } catch (e: NoSuchFieldException) {
            throw IllegalArgumentException(e)
        }

        if (fieldType?.isEnum == false || fieldType?.interfaces?.contains(PersistentEnum::class.java) == false) {
            throw IllegalArgumentException("Field [$fieldName] of class [$entityClass] is not an Set of PersistentEnum")
        }
    }

    override fun sqlTypes(): IntArray = intArrayOf(Types.OTHER)

    override fun returnedClass(): Class<*> = MutableSet::class.java

    @Throws(HibernateException::class)
    override fun equals(x: Any, y: Any): Boolean = Objects.equals(x, y)

    @Throws(HibernateException::class)
    override fun hashCode(x: Any): Int = Objects.hashCode(x)

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeGet(rs: ResultSet, names: Array<String>, session: SharedSessionContractImplementor, owner: Any): Any {
        val value = rs.getString(names[0])
        if (value == null || "{}" == value) {
            return hashSetOf<T>()
        }
        val split = value.substring(1, value.length - 1).split(",").toTypedArray()
        return Arrays.stream(split)
            .map { `val` -> fieldType!!.enumConstants.find { `val` == (it as PersistentEnum).value } }
            .collect(Collectors.toCollection { hashSetOf<T>() })
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(st: PreparedStatement, value: Any, index: Int, session: SharedSessionContractImplementor) {
        val set = value as MutableSet<*>
        val sqlValue: String = set.stream()
            .map { obj -> (obj as PersistentEnum).value }
            .collect(Collectors.joining(", ", "{", "}"))
        st.setObject(index, sqlValue, Types.OTHER)
    }

    @Throws(HibernateException::class)
    override fun deepCopy(value: Any): Any = (value as Set<*>).toMutableSet()

    override fun isMutable(): Boolean = true

    @Throws(HibernateException::class)
    override fun disassemble(value: Any): Serializable = value as Serializable

    @Throws(HibernateException::class)
    override fun assemble(cached: Serializable, owner: Any): Any = cached

    @Throws(HibernateException::class)
    override fun replace(original: Any?, target: Any?, owner: Any?): Any? = original
}
