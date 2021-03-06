package com.briolink.lib.common.type.graphql.scalar

import com.netflix.graphql.dgs.DgsScalar
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DgsScalar(name = "DateTime")
class DateTimeScalar : Coercing<LocalDateTime, String> {
    @Throws(CoercingSerializeException::class)
    override fun serialize(dataFetcherResult: Any): String {
        return if (dataFetcherResult is LocalDateTime) {
            dataFetcherResult.format(DateTimeFormatter.ISO_DATE_TIME)
        } else {
            throw CoercingSerializeException("Not a valid DateTime")
        }
    }

    @Throws(CoercingParseValueException::class)
    override fun parseValue(input: Any): LocalDateTime {
        return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_DATE_TIME)
    }

    @Throws(CoercingParseLiteralException::class)
    override fun parseLiteral(input: Any): LocalDateTime {
        if (input is StringValue) {
            return LocalDateTime.parse(input.value, DateTimeFormatter.ISO_DATE_TIME)
        }
        throw CoercingParseLiteralException("Value is not a valid ISO date time")
    }
}
