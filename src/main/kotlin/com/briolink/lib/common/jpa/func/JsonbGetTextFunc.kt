package com.briolink.lib.common.jpa.func

import com.briolink.lib.common.utils.joinToStringWithLastSeperator
import org.hibernate.QueryException
import org.hibernate.dialect.function.SQLFunction
import org.hibernate.engine.spi.Mapping
import org.hibernate.engine.spi.SessionFactoryImplementor
import org.hibernate.type.StringType
import org.hibernate.type.Type

class JsonbGetTextFunc : SQLFunction {
    @Throws(QueryException::class)
    override fun render(type: Type?, args: List<*>, sessionFactoryImplementor: SessionFactoryImplementor?): String {
        val field = args.first()
        val arguments = args.toMutableList().apply { removeFirst() }

        return "($field -> ${arguments.joinToStringWithLastSeperator(" -> ", " ->> ")})"
    }

    @Throws(QueryException::class)
    override fun getReturnType(columnType: Type?, mapping: Mapping?): Type {
        return StringType()
    }

    override fun hasArguments(): Boolean {
        return true
    }

    override fun hasParenthesesIfNoArguments(): Boolean {
        return false
    }
}
