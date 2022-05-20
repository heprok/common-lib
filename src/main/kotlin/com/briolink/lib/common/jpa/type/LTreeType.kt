package com.briolink.lib.common.jpa.type

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

class LTreeType : UserType {
    override fun sqlTypes(): IntArray = intArrayOf(Types.OTHER)

    override fun returnedClass(): Class<*> = String::class.java

    @Throws(HibernateException::class)
    override fun equals(x: Any?, y: Any?): Boolean = x == y

    @Throws(HibernateException::class)
    override fun hashCode(x: Any): Int = x.hashCode()

    @Throws(HibernateException::class)
    override fun deepCopy(value: Any?): Any? {
        if (value == null) return null
        check(value is String) { "Expected String, but got: " + value.javaClass }
        return value
    }

    override fun isMutable(): Boolean = false

    @Throws(HibernateException::class)
    override fun disassemble(value: Any?): Serializable? = value as Serializable?

    @Throws(HibernateException::class)
    override fun assemble(cached: Serializable?, owner: Any?): Any? = cached

    @Throws(HibernateException::class)
    override fun replace(original: Any?, target: Any?, owner: Any?): Any? = deepCopy(original)

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeGet(
        rs: ResultSet,
        names: Array<String?>,
        session: SharedSessionContractImplementor?,
        owner: Any?
    ): Any? = rs.getString(names[0])

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(
        st: PreparedStatement,
        value: Any?,
        index: Int,
        session: SharedSessionContractImplementor?
    ) = st.setObject(index, value, Types.OTHER)
}
