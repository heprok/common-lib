package com.briolink.lib.common

import com.briolink.lib.common.exception.AccessDeniedException
import com.briolink.lib.common.exception.BadRequestException
import com.briolink.lib.common.exception.EntityExistException
import com.briolink.lib.common.exception.EntityNotFoundException
import com.briolink.lib.common.exception.UnavailableException
import com.briolink.lib.common.exception.ValidationException
import com.briolink.lib.common.exception.base.BaseAccessDeniedException
import com.briolink.lib.common.exception.base.BaseBadRequestException
import com.briolink.lib.common.exception.base.BaseExistException
import com.briolink.lib.common.exception.base.BaseNotFoundException
import com.briolink.lib.common.exception.base.BaseUnavailableException
import com.briolink.lib.common.exception.base.BaseValidationException
import com.briolink.lib.common.exception.base.IBlException
import com.briolink.lib.common.type.basic.BlErrorResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.ConstraintViolationException

@ControllerAdvice
@ConditionalOnMissingBean(annotation = [ControllerAdvice::class])
@Order(Ordered.LOWEST_PRECEDENCE)
@RequestMapping(produces = ["application/json"])
open class BlRestGlobalExceptionHandler(private val lm: BlLocaleMessage) {

    protected open fun getResponseEntityWithTranslateMessage(ex: IBlException): ResponseEntity<BlErrorResponse> {
        val message = lm.getMessage(ex.code, ex.arguments)
        return ResponseEntity<BlErrorResponse>(BlErrorResponse(message, ex.httpsStatus), ex.httpsStatus)
    }

    @ExceptionHandler(value = [BaseExistException::class, EntityExistException::class])
    open fun existsException(ex: IBlException): ResponseEntity<BlErrorResponse> {
        return getResponseEntityWithTranslateMessage(ex)
    }

    @ExceptionHandler(value = [BaseNotFoundException::class, EntityNotFoundException::class])
    open fun notFoundException(ex: IBlException): ResponseEntity<BlErrorResponse> {
        return getResponseEntityWithTranslateMessage(ex)
    }

    @ExceptionHandler(value = [BaseValidationException::class, ValidationException::class])
    open fun validationException(ex: IBlException): ResponseEntity<BlErrorResponse> {
        return getResponseEntityWithTranslateMessage(ex)
    }

    @ExceptionHandler(value = [BaseBadRequestException::class, BadRequestException::class])
    open fun badRequestException(ex: IBlException): ResponseEntity<BlErrorResponse> {
        return getResponseEntityWithTranslateMessage(ex)
    }

    @ExceptionHandler(value = [BaseAccessDeniedException::class, AccessDeniedException::class])
    open fun accessDeniedException(ex: IBlException): ResponseEntity<BlErrorResponse> {
        return getResponseEntityWithTranslateMessage(ex)
    }

    @ExceptionHandler(value = [org.springframework.security.access.AccessDeniedException::class])
    open fun accessDeniedException(ex: org.springframework.security.access.AccessDeniedException): ResponseEntity<BlErrorResponse> {
        return getResponseEntityWithTranslateMessage(
            (ex.message?.let { AccessDeniedException(it) } ?: AccessDeniedException()) as IBlException
        )
    }

    @ExceptionHandler(value = [BaseUnavailableException::class, UnavailableException::class])
    open fun unavailableException(ex: IBlException): ResponseEntity<BlErrorResponse> {
        return getResponseEntityWithTranslateMessage(ex)
    }

    @ExceptionHandler(value = [BindException::class])
    open fun validationBindException(ex: BindException): ResponseEntity<BlErrorResponse> {

        val msg = ex.fieldErrors.joinToString("\n") {
            it.field + ": " + it.defaultMessage?.let { key -> lm.getMessage(key) }
        }
        return ResponseEntity(BlErrorResponse(message = msg, httpsStatus = HttpStatus.NOT_ACCEPTABLE), HttpStatus.NOT_ACCEPTABLE)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    open fun validationException(ex: ConstraintViolationException): ResponseEntity<BlErrorResponse> {
        return ResponseEntity(
            BlErrorResponse(
                message = ex.message?.let { lm.getMessage(it) },
                httpsStatus = HttpStatus.NOT_ACCEPTABLE
            ),
            HttpStatus.NOT_ACCEPTABLE
        )
    }
}
