package com.briolink.lib.common.jpa.func

import org.hibernate.QueryException
import org.hibernate.dialect.function.SQLFunction
import org.hibernate.engine.spi.Mapping
import org.hibernate.engine.spi.SessionFactoryImplementor
import org.hibernate.type.BooleanType
import org.hibernate.type.Type

class ArrContainsAny : SQLFunction {
    @Throws(QueryException::class)
    override fun render(type: Type?, args: List<*>, sessionFactoryImplementor: SessionFactoryImplementor?): String {
        if (args.count() != 3) throw RuntimeException("arr_contains_any has invalid arguments")
        return "${args[0]} && ${args[1]}::${args[2].toString().replace("'", "")}[]"
    }

    @Throws(QueryException::class)
    override fun getReturnType(columnType: Type?, mapping: Mapping?): Type = BooleanType()
    override fun hasArguments(): Boolean = true
    override fun hasParenthesesIfNoArguments(): Boolean = false
}
