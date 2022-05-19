package com.briolink.lib.common.type.graphql.scalar

import com.netflix.graphql.dgs.DgsScalar
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.util.UUID

@DgsScalar(name = "UUID")
class UUIDScalar : Coercing<UUID, String> {
    @Throws(CoercingSerializeException::class)
    override fun serialize(dataFetcherResult: Any): String =
        if (dataFetcherResult is UUID) dataFetcherResult.toString() else throw CoercingSerializeException("Invalid UUID string")

    @Throws(CoercingParseValueException::class)
    override fun parseValue(input: Any): UUID =
        try {
            UUID.fromString(input.toString())
        } catch (e: Exception) {
            throw CoercingParseValueException("Invalid UUID string: $input")
        }

    @Throws(CoercingParseLiteralException::class)
    override fun parseLiteral(input: Any): UUID =
        try {
            val uuid = when (input) {
                is StringValue -> input.value
                else -> null
            }

            UUID.fromString(uuid)
        } catch (e: Exception) {
            throw CoercingParseLiteralException("Invalid UUID string")
        }
}
