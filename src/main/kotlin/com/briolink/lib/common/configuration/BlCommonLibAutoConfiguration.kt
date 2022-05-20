package com.briolink.lib.common.configuration

import com.briolink.lib.common.BlDataFetcherExceptionHandler
import com.briolink.lib.common.BlLocaleMessage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

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
            "/i18n/validation",
            "classpath*:/i18n/validation",
        )
        return BlLocaleMessage(source)
    }

    @Bean
    fun blDataFetcherExceptionHandler() = BlDataFetcherExceptionHandler(blLocaleMessage())
}
