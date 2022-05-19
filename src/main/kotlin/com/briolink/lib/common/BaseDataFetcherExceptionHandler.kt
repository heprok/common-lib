package com.briolink.lib.common

import com.briolink.lib.common.exception.BaseAccessDeniedException
import com.briolink.lib.common.exception.BaseExistsException
import com.briolink.lib.common.exception.BaseNotFoundException
import com.briolink.lib.common.exception.ExceptionInterface
import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.netflix.graphql.types.errors.ErrorType
import com.netflix.graphql.types.errors.TypedGraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.RequestPredicates.path
import javax.validation.ConstraintViolationException

open class BaseDataFetcherExceptionHandler(localeMessage: BaseLocaleMessage) : DataFetcherExceptionHandler {
    init {
        lm = localeMessage
    }

    private val defaultHandler = DefaultDataFetcherExceptionHandler()

    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        val exception = handlerParameters.exception
        val path = handlerParameters.path

        val error: TypedGraphQLError = when (exception) {
            is BaseNotFoundException ->
                TypedGraphQLError.newBuilder().errorType(ErrorType.NOT_FOUND)
                    .message(lm.getMessage(exception.code))
                    .path(path)
                    .build()
            is BaseAccessDeniedException -> TypedGraphQLError.newBuilder().errorType(ErrorType.NOT_FOUND)
                .message(lm.getMessage(exception.code))
                .path(path)
                .build()
            is DgsEntityNotFoundException ->
                TypedGraphQLError.newBuilder().errorType(ErrorType.NOT_FOUND)
                    .message(exception.message)
                    .path(path)
                    .build()
            is BaseExistsException ->
                TypedGraphQLError.newBuilder().errorType(ErrorType.INTERNAL)
                    .message(lm.getMessage(exception.code))
                    .path(path)
                    .build()
            is ExceptionInterface -> {
                val errorType: ErrorType = when (exception.httpsStatus) {
                    HttpStatus.NOT_FOUND -> ErrorType.NOT_FOUND
                    HttpStatus.FORBIDDEN -> ErrorType.PERMISSION_DENIED
                    HttpStatus.BAD_REQUEST -> ErrorType.BAD_REQUEST
                    HttpStatus.UNAUTHORIZED -> ErrorType.UNAUTHENTICATED
                    else -> {
                        if (exception.httpsStatus.is4xxClientError) ErrorType.BAD_REQUEST
                        else ErrorType.INTERNAL
                    }
                }

                TypedGraphQLError.newBuilder().errorType(errorType)
                    .message(lm.getMessage(exception.code))
                    .path(path)
                    .build()
            }
            else ->
                return defaultHandler.handleException(handlerParameters).get()
        }

        return DataFetcherExceptionHandlerResult.newResult().error(error).build()
    }

    companion object {
        lateinit var lm: BaseLocaleMessage

        fun mapUserErrors(cve: ConstraintViolationException): List<Error> {
            val errors: MutableList<Error> = mutableListOf()

            for (violation in cve.constraintViolations) {
                errors.add(Error(lm.getMessage(violation.message)))
            }

            return errors
        }

        private fun getParam(s: String): String {
            val splits = s.split("\\.".toRegex()).toTypedArray()
            return if (splits.size == 1) s else java.lang.String.join(".", splits.copyOfRange(2, splits.size).toList())
        }
    }
}
