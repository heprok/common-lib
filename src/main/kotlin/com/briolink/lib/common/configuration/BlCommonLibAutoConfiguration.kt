package com.briolink.lib.common.configuration

import com.briolink.lib.common.BlDataFetcherExceptionHandler
import com.briolink.lib.common.BlLocaleMessage
import com.briolink.lib.common.utils.BlServletUtils
import com.briolink.lib.common.utils.StringUtils
import mu.KLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import javax.servlet.http.HttpServletRequest

@Configuration
@ComponentScans(value = [ComponentScan("com.briolink.lib.common", "com.briolink.lib.common.utils")])
@ConfigurationProperties(prefix = "spring.messages.basename", ignoreInvalidFields = true, ignoreUnknownFields = true)
class BlCommonLibAutoConfiguration {

    private var basename: String? = null

    companion object : KLogging()

    @Bean
    fun blLocaleMessage(): BlLocaleMessage {
        val source = ReloadableResourceBundleMessageSource()

        val basenameArr = basename?.split(",")?.map { StringUtils.trimAllSpaces(it) }?.also {
            logger.info { "Loading messages from: $it" }
        }

        source.setDefaultEncoding("UTF-8")
        source.addBasenames(
            "/i18n/exception",
            "/i18n/validation",
        )
        if (basenameArr != null) source.addBasenames(*basenameArr.toTypedArray())

        return BlLocaleMessage(source)
    }

    @Bean
    fun blDataFetcherExceptionHandler(blLocaleMessage: BlLocaleMessage) = BlDataFetcherExceptionHandler(blLocaleMessage)

    // @Bean
    // fun blRestGlobalExceptionHandler(blLocaleMessage: BlLocaleMessage) = BlRestGlobalExceptionHandler(blLocaleMessage)

    @Bean
    fun blServletUtils(request: HttpServletRequest) = BlServletUtils(request)
}
