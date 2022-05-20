package com.briolink.lib.common.exception

import com.briolink.lib.common.exception.base.BaseNotFoundException

class EntityNotFoundException(override val message: String = "Entity not found") : BaseNotFoundException() {
    override val code: String = "exception.entity.not-found"
}
