package com.briolink.lib.common.configuration

import com.briolink.lib.common.BaseDataFetcherExceptionHandler
import com.briolink.lib.common.util.BlLocaleMessage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
@ComponentScan("com.briolink.lib.common")
@PropertySource("classpath:/i18n2/messages1.properties")
class BlCommonLibAutoConfiguration {
    @Bean
    @Qualifier("external")
    fun baseLocaleMessage(): BlLocaleMessage {
        val source = ReloadableResourceBundleMessageSource()
        source.setDefaultEncoding("UTF-8")
        source.setBasenames(
            "classpath:i18n2/messages1.properties",
            "classpath:com/briolink/lib/i18n2/messages1.properties"
        )
        return BlLocaleMessage(source)
    }

    @Bean
    fun baseDataFetcherExceptionHandler() = BaseDataFetcherExceptionHandler(baseLocaleMessage())
}
