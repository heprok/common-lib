package com.briolink.lib.common.utils

import javax.servlet.http.HttpServletRequest

open class BlServletUtils(private val request: HttpServletRequest) {
    protected open val intranetServerNamePattern = "[\\w-]+\\.[\\w-]+\\.svc\\.cluster\\.local$".toRegex()

    open fun isIntranet(): Boolean = intranetServerNamePattern.matches(request.serverName)
}
