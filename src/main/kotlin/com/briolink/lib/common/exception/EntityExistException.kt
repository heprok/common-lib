package com.briolink.lib.common.exception

import com.briolink.lib.common.exception.base.BaseExistException

class EntityExistException(override val message: String = "Entity exist") : BaseExistException() {
    override val code: String = "exception.entity.exist"
}
