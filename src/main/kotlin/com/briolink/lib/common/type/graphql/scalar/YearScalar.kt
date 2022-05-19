package com.briolink.lib.common.type.graphql.scalar

import com.netflix.graphql.dgs.DgsScalar
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.Year

@DgsScalar(name = "Year")
class YearScalar : Coercing<Year, Int> {
    @Throws(CoercingSerializeException::class)
    override fun serialize(dataFetcherResult: Any): Int =
        if (dataFetcherResult is Year) dataFetcherResult.value else throw CoercingSerializeException("Not a valid Year")

    @Throws(CoercingParseValueException::class)
    override fun parseValue(input: Any): Year =
        try {
            Year.parse(input.toString())
        } catch (e: Exception) {
            throw CoercingParseValueException("Value is not a valid ISO-8601 year")
        }

    @Throws(CoercingParseLiteralException::class)
    override fun parseLiteral(input: Any): Year =
        try {
            val year = when (input) {
                is StringValue -> input.value
                is IntValue -> input.value.toString()
                else -> null
            }

            Year.parse(year)
        } catch (e: Exception) {
            throw CoercingParseLiteralException("Value is not a valid ISO-8601 year")
        }
}
