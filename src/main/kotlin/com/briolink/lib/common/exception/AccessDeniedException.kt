package com.briolink.lib.common.exception

import com.briolink.lib.common.exception.base.BaseAccessDeniedException

class AccessDeniedException(override val message: String = "Access denied") : BaseAccessDeniedException() {
    override val code: String = "exception.access-denied"
}
