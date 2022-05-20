package com.briolink.lib.common.util

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.format.FormatDetector
import com.sksamuel.scrimage.nio.PngWriter
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object ImageUtil {
    val PNG_WRITER = PngWriter().withMinCompression()

    fun getFormat(bytes: ByteArray): String? {
        var format = FormatDetector.detect(bytes).orElse(null)?.name?.lowercase()

        if (format == null) {
            val iis = ImageIO.createImageInputStream(ByteArrayInputStream(bytes))
            val readers = ImageIO.getImageReaders(iis)
            while (readers.hasNext()) {
                format = readers.next().formatName
                break
            }
        }

        return format
    }

    fun toPng(bytes: ByteArray, width: Int? = null, height: Int? = null): ByteArray {
        val image = ImmutableImage.loader().fromBytes(bytes)

        if (width != null && height != null) {
            image.resizeTo(width, height)
        } else if (width != null) {
            image.resizeToWidth(width)
        } else if (height != null) {
            image.resizeToHeight(height)
        }

        return image.bytes(PNG_WRITER)
    }
}
