package com.briolink.lib.common.configuration

import com.briolink.lib.common.configuration.condition.S3ClientPresentCondition
import com.briolink.lib.common.util.BlS3Util
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.s3.S3Client

@Configuration
@ComponentScan("com.briolink.lib.common.util")
@ConditionalOnProperty(prefix = "app.aws.s3", name = ["name"])
class BlS3UtilAutoConfiguration {

    @Value("\${app.aws.s3.name}")
    lateinit var s3BucketName: String

    @Bean
    @Conditional(S3ClientPresentCondition::class)
    fun blS3Util() = BlS3Util(s3BucketName, S3Client.create())
}
