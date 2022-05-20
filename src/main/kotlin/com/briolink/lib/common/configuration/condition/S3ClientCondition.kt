package com.briolink.lib.common.configuration.condition

import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class S3ClientPresentCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        return try {
            Class.forName("software.amazon.awssdk.services.s3.S3Client")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}
class S3ClientNotPresentCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        return try {
            Class.forName("software.amazon.awssdk.services.s3.S3Client")
            false
        } catch (e: ClassNotFoundException) {
            true
        }
    }
}
