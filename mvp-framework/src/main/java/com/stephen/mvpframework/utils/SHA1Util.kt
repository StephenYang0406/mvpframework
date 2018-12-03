package com.stephen.mvpframework.utils

import java.security.MessageDigest

/**
 * SHA1工具类
 * Created by Stephen on 2018/11/19.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object SHA1Util {
    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private fun getFormattedText(bytes: ByteArray): String {
        val len = bytes.size
        val buf = StringBuilder(len * 2)
        // 把密文转换成十六进制的字符串形式
        for (j in 0 until len) {
            buf.append(HEX_DIGITS[(bytes[j].toInt() shr 4) and 0x0f])
            buf.append(HEX_DIGITS[bytes[j].toInt() and 0x0f])
        }
        return buf.toString()
    }

    fun encode(str: String): String {
        try {
            val messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.update(str.toByteArray())
            return getFormattedText(messageDigest.digest())
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}