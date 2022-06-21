package com.briolink.lib.common

import com.briolink.lib.common.exception.base.IBlException
import com.briolink.lib.common.validation.StringInList
import com.netflix.graphql.dgs.exceptions.DgsBadRequestException
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.netflix.graphql.types.errors.ErrorType
import com.netflix.graphql.types.errors.TypedGraphQLError
import com.netflix.graphql.types.errors.TypedGraphQLError.Builder
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import graphql.execution.ResultPath
import graphql.language.SourceLocation
import mu.KLogging
import org.springframework.http.HttpStatus
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.CompletableFuture
import javax.validation.ConstraintViolationException

open class BlDataFetcherExceptionHandler(localeMessage: BlLocaleMessage) : DataFetcherExceptionHandler {
    init {
        lm = localeMessage
    }

    @Deprecated("Deprecated in Java")
    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        var exception = handlerParameters.exception
        if (exception is InvocationTargetException) exception = exception.targetException
        val location = handlerParameters.sourceLocation
        val path = handlerParameters.path

        logger.error("Exception while executing data fetcher for ${handlerParameters.path}: ${exception.message}", exception)

        val graphqlError: GraphQLError = when (exception) {
            is DgsEntityNotFoundException -> getGraphqlError(TypedGraphQLError.newNotFoundBuilder(), location, path, exception)
            is DgsBadRequestException -> getGraphqlError(TypedGraphQLError.newBadRequestBuilder(), location, path, exception)
            is org.springframework.security.access.AccessDeniedException ->
                getGraphqlError(TypedGraphQLError.newPermissionDeniedBuilder(), location, path, exception)
            is IllegalArgumentException -> getGraphqlError(TypedGraphQLError.newInternalErrorBuilder(), location, path, exception)
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

                val message = lm.getMessage(exception.code, exception.arguments).let {
                    if (!exception.message.isNullOrBlank() && it == exception.code) lm.getMessage(exception.message!!) else it
                }

                getGraphqlError(TypedGraphQLError.newBuilder().errorType(errorType), location, path, message)
            }
            else -> {
                logger.error("Exception while executing data fetcher for ${handlerParameters.path}: ${exception.message}", exception)
                getGraphqlError(TypedGraphQLError.newInternalErrorBuilder(), location, path, exception)
            }
        }

        return DataFetcherExceptionHandlerResult.newResult().error(graphqlError).build()
    }

    protected open fun getGraphqlError(
        builder: Builder,
        sourceLocation: SourceLocation,
        path: ResultPath,
        exception: Throwable
    ): GraphQLError {
        return builder
            .location(sourceLocation)
            .message("%s", exception.message?.let { lm.getMessage(it) })
            .path(path).build()
    }

    protected open fun getGraphqlError(
        builder: Builder,
        sourceLocation: SourceLocation,
        path: ResultPath,
        message: String
    ): GraphQLError {
        return builder
            .location(sourceLocation)
            .message("%s", lm.getMessage(message))
            .path(path).build()
    }

    override fun handleException(
        handlerParameters: DataFetcherExceptionHandlerParameters
    ): CompletableFuture<DataFetcherExceptionHandlerResult> = CompletableFuture.completedFuture(onException(handlerParameters))

    companion object {
        val logger = KLogging().logger
        lateinit var lm: BlLocaleMessage

        fun mapUserErrors(cve: ConstraintViolationException): List<Error> {
            val errors: MutableList<Error> = mutableListOf()

            for (violation in cve.constraintViolations) {
                if (violation.constraintDescriptor.annotation.annotationClass == StringInList::class) {
                    val annotation = violation.constraintDescriptor.annotation as StringInList
                    val message = lm.getMessage(annotation.message, annotation.allowedValues.joinToString())

                    errors.add(Error(message))
                } else
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
