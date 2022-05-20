package com.briolink.lib.common.configuration

import com.briolink.lib.common.BlDataFetcherExceptionHandler
import com.briolink.lib.common.BlLocaleMessage
import com.briolink.lib.common.util.BlServletUtil
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import javax.servlet.http.HttpServletRequest

@Configuration
@ComponentScan("com.briolink.lib.common")
class BlCommonLibAutoConfiguration {
    @Bean
    @Qualifier("external")
    fun blLocaleMessage(): BlLocaleMessage {
        val source = ReloadableResourceBundleMessageSource()
        source.setDefaultEncoding("UTF-8")
        source.addBasenames(
            "classpath:messages",
            "/i18n/exception",
            "classpath*:/i18n/exception",
            "/i18n/validation",
            "classpath*:/i18n/validation",
        )
        return BlLocaleMessage(source)
    }

    @Bean
    fun blDataFetcherExceptionHandler(blLocaleMessage: BlLocaleMessage) = BlDataFetcherExceptionHandler(blLocaleMessage)

    @Bean
    fun blServletUtil(request: HttpServletRequest) = BlServletUtil(request)
}
