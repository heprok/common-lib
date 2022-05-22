package com.briolink.lib.common.utils

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

object BlSecurityUtils {
    /**
     * Get the current user's id.
     * @throws ClassCastException if the current user is not authenticated.
     * @return the current user's id
     */
    @get:Throws(ClassCastException::class)
    val currentUserId: UUID
        get() {
            val authentication: JwtAuthenticationToken =
                SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
            return UUID.fromString(authentication.token.subject)
        }

    val isGuest: Boolean
        get() = SecurityContextHolder.getContext().authentication is AnonymousAuthenticationToken
}
