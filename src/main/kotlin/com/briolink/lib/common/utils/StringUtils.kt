package com.briolink.lib.common.utils

import com.ibm.icu.text.Transliterator
import java.net.URL
import java.util.Locale

private val toAnyLatin =
    Transliterator.getInstance("Cyrillic-Latin; Any-Latin; Latin-ASCII; Any-Lower")
private val allowedCharPool: List<Char> = ('a'..'z') + ('0'..'9')

object StringUtils {
    fun slugify(str: String, withRandom: Boolean = false, length: Int = 50): String =
        toAnyLatin
            .transliterate(str.trim())
            .replace("[^\\p{ASCII}]".toRegex(), "")
            .replace("[\\W\\s+]+".toRegex(), "-")
            .replace("^-|-$".toRegex(), "")
            .lowercase(Locale.getDefault())
            .let {
                if (withRandom)
                    "${it.take(length - 5)}-${(1..4).map { allowedCharPool.random() }.joinToString("")}"
                else it.take(length)
            }

    fun replaceNonWord(str: String, replaceSymbol: String = " "): String =
        str.replace("[^\\p{L}\\p{N}_]+", replaceSymbol)

    fun trimAllSpaces(str: String) = str.trim().replace(Regex("[\\s]{2,}"), " ")
    fun prepareUrl(str: String?): URL? = str?.let {
        URL("https://" + trimAllSpaces(it).replace(Regex("^https?:\\/\\/"), "").replace(Regex("^www."), ""))
    }

    fun prepareUrl(url: URL?): URL? = prepareUrl(url?.toString())
}

fun <T> List<T>.joinToStringWithLastSeperator(
    separator: String,
    lastSeparator: String
): String {
    @Suppress("MagicNumber")
    val assumedAverageLength = 4
    val sb = StringBuilder(size * assumedAverageLength)

    if (size > 0) {
        sb.append(this[0])
    }

    for (i in 1 until size - 1) {
        sb.append(separator)
        sb.append(this[i])
    }

    if (size > 1) {
        sb.append(lastSeparator)
        sb.append(this[size - 1])
    }

    return sb.toString()
}
