package com.briolink.lib.common.util

import com.briolink.lib.common.exception.FileTypeException
import mu.KLogging
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.transfer.s3.S3TransferManager
import software.amazon.awssdk.transfer.s3.UploadFileRequest
import java.net.URL
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.pathString

open class BlS3Util(
    private val bucketName: String,
    private val s3Client: S3Client
) {
    companion object : KLogging() {
        val ALLOWED_IMAGE_TYPES: Map<String, Pair<String, String>> = mapOf(
            "jpeg" to Pair("jpg", "image/jpeg"),
            "png" to Pair("png", "image/png"),
            "gif" to Pair("gif", "image/gif"),
            "svg" to Pair("svg", "image/svg+xml"),
            "webp" to Pair("webp", "image/webp")
        )
        val DEFAULT_HEADER_ACL_VALUE: ObjectCannedACL = ObjectCannedACL.PUBLIC_READ
        lateinit var BUCKET_URL: URL
    }

    init {
        BUCKET_URL = URL("https://$bucketName.s3.amazonaws.com")
    }

    fun uploadFile(
        bytes: ByteArray,
        objectKey: String,
        contentType: String,
        contentLength: Long? = null,
        objectAcl: ObjectCannedACL? = null
    ): URL =
        try {
            val cntLength = contentLength ?: bytes.size.toLong()
            val metadataVal = mutableMapOf<String, String>()
            metadataVal["contentType"] = contentType
            metadataVal["contentLength"] = cntLength.toString()

            val request = PutObjectRequest
                .builder()
                .metadata(metadataVal)
                .cacheControl("max-age=15552000")
                .bucket(bucketName)
                .key(objectKey)
                .acl(objectAcl ?: DEFAULT_HEADER_ACL_VALUE)
                .build()

            s3Client.putObject(request, RequestBody.fromBytes(bytes))
            s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(objectKey).build())
        } catch (ioe: Exception) {
            logger.error("Error uploading file to S3: $objectKey", ioe)
            throw ioe
        }

    fun uploadFile(
        file: MultipartFile,
        objectKey: String,
        contentType: String,
        contentLength: Long? = null,
        objectAcl: ObjectCannedACL? = null
    ): URL = uploadFile(file.bytes, objectKey, contentType, contentLength, objectAcl)

    fun uploadImage(path: String, file: MultipartFile): URL = uploadImage(path, file.bytes)
    fun uploadImage(path: String, url: URL): URL = uploadImage(path, url.openConnection().inputStream.readAllBytes())

    fun uploadImage(path: String, bytesArray: ByteArray): URL {
        val format = ImageUtil.getFormat(bytesArray)

        return if (ALLOWED_IMAGE_TYPES.containsKey(format)) {
            var type = ALLOWED_IMAGE_TYPES[format]
            var bytes = bytesArray

            if (format == "gif" || format == "svg") {
                type = ALLOWED_IMAGE_TYPES["png"]
                bytes = ImageUtil.toPng(bytes, if (format == "svg") 512 else null)
            }

            uploadFile(bytes, "$path/${genObjectKey()}.${type!!.first}", type.second)
        } else {
            throw FileTypeException()
        }
    }

    private fun genObjectKey(): String = UUID.randomUUID().toString().replace("-", "")

    fun uploadDirectory(path: String, dir: Path, objectAcl: ObjectCannedACL? = null): URL {
        val tm = S3TransferManager.builder().build()

        dir.toFile().walk().forEach {
            if (!it.isFile) return@forEach

            val key = "${path}${it.path.replace(dir.pathString, "")}".replace("\\", "/").replace(Regex("(/{2,})+"), "/")
            val request = UploadFileRequest
                .builder()
                .source(it)
                .putObjectRequest(
                    PutObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(key)
                        .acl(objectAcl ?: DEFAULT_HEADER_ACL_VALUE)
                        .build()
                )
                .build()

            val test = tm.uploadFile(request)
            test.completionFuture().join()
        }

        return URL("https://$bucketName.s3.amazonaws.com/$path")
    }
}
