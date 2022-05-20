package com.briolink.lib.common.jpa

import com.briolink.lib.common.jpa.func.ArrayAggFunc
import com.briolink.lib.common.jpa.func.ConcatWsFunc
import com.briolink.lib.common.jpa.func.DateRangeFunc
import com.briolink.lib.common.jpa.func.JsonbGetFunc
import com.briolink.lib.common.jpa.func.JsonbSetsFunc
import org.hibernate.QueryException
import org.hibernate.boot.MetadataBuilder
import org.hibernate.boot.spi.MetadataBuilderContributor
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.engine.spi.Mapping
import org.hibernate.type.BooleanType
import org.hibernate.type.StringType
import org.hibernate.type.Type

open class Functions : MetadataBuilderContributor {
    override fun contribute(metadataBuilder: MetadataBuilder) {
        metadataBuilder.applySqlFunction(
            "fts_partial",
            SQLFunctionTemplate(
                BooleanType.INSTANCE,
                "to_tsvector('simple', ?1) @@ to_tsquery(quote_literal(quote_literal(?2)) || ':*')"
            ),
        )
        metadataBuilder.applySqlFunction(
            "fts_partial_col",
            SQLFunctionTemplate(BooleanType.INSTANCE, "?1 @@ to_tsquery(quote_literal(quote_literal(?2)) || ':*')"),
        )

        metadataBuilder.applySqlFunction("tsv", SQLFunctionTemplate(StringType.INSTANCE, "to_tsvector('simple', ?1)"))

        metadataBuilder.applySqlFunction(
            "orderby_equal",
            SQLFunctionTemplate(BooleanType.INSTANCE, "?1 = ?2"),
        )
        metadataBuilder.applySqlFunction(
            "array_append",
            object : SQLFunctionTemplate(null, "array_append(?1, ?2)") {
                @Throws(QueryException::class)
                override fun getReturnType(argumentType: Type?, mapping: Mapping?): Type? {
                    return argumentType
                }
            },
        )
        metadataBuilder.applySqlFunction(
            "array_remove",
            object : SQLFunctionTemplate(null, "array_remove(?1, ?2)") {
                @Throws(QueryException::class)
                override fun getReturnType(argumentType: Type?, mapping: Mapping?): Type? {
                    return argumentType
                }
            },
        )
        metadataBuilder.applySqlFunction(
            "array_contains_element",
            SQLFunctionTemplate(BooleanType.INSTANCE, "?1 @> array[?2]"),
        )

        metadataBuilder.applySqlFunction(
            "array_contains_common_element",
            SQLFunctionTemplate(BooleanType.INSTANCE, "?1 && array[?2]"),
        )

        metadataBuilder.applySqlFunction(
            "int4range_contains",
            SQLFunctionTemplate(BooleanType.INSTANCE, "?1 <@ int4range(?2, ?3)"),
        )

        metadataBuilder.applySqlFunction("daterange_cross", DateRangeFunc())
        metadataBuilder.applySqlFunction("jsonb_sets", JsonbSetsFunc())
        metadataBuilder.applySqlFunction("jsonb_get", JsonbGetFunc())
        metadataBuilder.applySqlFunction("array_agg", ArrayAggFunc())
        metadataBuilder.applySqlFunction("concat_ws", ConcatWsFunc())
    }
}
