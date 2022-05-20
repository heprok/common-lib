package com.briolink.lib.common.util

import java.awt.Color

object ColorUtil {
    fun luminance(color: Color) = 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) / 255
    fun isLight(color: Color): Boolean = luminance(color) > 0.5
    fun isDark(color: Color): Boolean = !isLight(color)
}
