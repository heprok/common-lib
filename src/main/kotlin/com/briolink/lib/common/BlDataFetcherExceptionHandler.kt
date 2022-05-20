package com.briolink.lib.common

import com.briolink.lib.common.exception.IBlException
import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.netflix.graphql.types.errors.ErrorType
import com.netflix.graphql.types.errors.TypedGraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.http.HttpStatus
import javax.validation.ConstraintViolationException

open class BlDataFetcherExceptionHandler(localeMessage: BlLocaleMessage) : DataFetcherExceptionHandler {
    init {
        lm = localeMessage
    }

    protected open val defaultHandler = DefaultDataFetcherExceptionHandler()

    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        val exception = handlerParameters.exception
        val path = handlerParameters.path
        val location = handlerParameters.sourceLocation

        val error: TypedGraphQLError = when (exception) {
            is DgsEntityNotFoundException ->
                TypedGraphQLError.newBuilder().errorType(ErrorType.NOT_FOUND)
                    .message(exception.message)
                    .path(path)
                    .location(location)
                    .build()

            is IBlException -> {
                val errorType: ErrorType = when (exception.httpsStatus) {
                    HttpStatus.NOT_FOUND -> ErrorType.NOT_FOUND
                    HttpStatus.FORBIDDEN -> ErrorType.PERMISSION_DENIED
                    HttpStatus.BAD_REQUEST -> ErrorType.BAD_REQUEST
                    HttpStatus.UNAUTHORIZED -> ErrorType.UNAUTHENTICATED
                    HttpStatus.SERVICE_UNAVAILABLE -> ErrorType.UNAVAILABLE
                    else -> {
                        if (exception.httpsStatus.is4xxClientError) ErrorType.BAD_REQUEST
                        else ErrorType.INTERNAL
                    }
                }

                val message = lm.getMessage(exception.code).let {
                    if (!exception.message.isNullOrBlank() && it == exception.code) lm.getMessage(exception.message!!) else it
                }

                TypedGraphQLError.newBuilder().errorType(errorType)
                    .message(message)
                    .path(path)
                    .location(location)
                    .build()
            }
            else ->
                return defaultHandler.handleException(handlerParameters).get()
        }

        return DataFetcherExceptionHandlerResult.newResult().error(error).build()
    }

    companion object {
        lateinit var lm: BlLocaleMessage

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
