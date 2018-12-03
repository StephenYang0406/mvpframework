package com.stephen.mvpframework.network.interceptor

import com.stephen.mvpframework.utils.LogUtil
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Created by Stephen on 2018/11/28.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class HttpLoggingInterceptor : Interceptor {
    @Suppress("PrivatePropertyName")
    private val UTF_8 = Charset.forName("UTF-8")
    private var logger: Logger
    @Volatile
    private var level: Level

    constructor() : this(Logger.DEFAULT)
    constructor(logger: Logger) {
        this.level = Level.NONE
        this.logger = logger
    }

    internal fun setLevel(level: Level): HttpLoggingInterceptor {
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return this.level
    }

    override fun intercept(chain: Interceptor.Chain): Response? {
        val level = this.level
        val request = chain.request()
        if (level == Level.NONE) {
            return try {
                chain.proceed(request)
            } catch (e: IOException) {
                null
            }
        } else {
            val logBody = level == Level.BODY
            val logHeaders = logBody || level == Level.HEADERS
            val requestBody = request.body()
            val hasRequestBody = requestBody != null
            val connection = chain.connection()
            val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
            var requestStartMessage = "--> " + request.method() + ' '.toString() + request.url() + ' '.toString() + protocol
            val requestContentLength: Long
            requestContentLength = try {
                requestBody!!.contentLength()
            } catch (e: IOException) {
                e.printStackTrace()
                -1
            } catch (e: NullPointerException) {
                e.printStackTrace()
                -1
            }

            if (!logHeaders && hasRequestBody) {
                requestStartMessage = "$requestStartMessage ($requestContentLength-byte body)"
            }

            this.logger.log(requestStartMessage)
            if (logHeaders) {
                if (hasRequestBody) {
                    if (requestBody!!.contentType() != null) {
                        this.logger.log("Content-Type: " + requestBody.contentType()!!)
                    }

                    if (requestContentLength != -1L) {
                        this.logger.log("Content-Length: $requestContentLength")
                    }
                }

                val headers = request.headers()
                var i = 0

                val count = headers.size()
                while (i < count) {
                    val name = headers.name(i)
                    if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                        this.logger.log(name + ": " + headers.value(i))
                    }
                    ++i
                }

                if (logBody && hasRequestBody) {
                    if (this.bodyEncoded(request.headers())) {
                        this.logger.log("--> END " + request.method() + " (encoded body omitted)")
                    } else {
                        val buffer = Buffer()
                        try {
                            requestBody!!.writeTo(buffer)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            LogUtil.testError("requestBody.writeTo(buffer)---->error")
                        }

                        var charset: Charset? = UTF_8
                        val contentType = requestBody!!.contentType()
                        if (contentType != null) {
                            charset = contentType.charset(UTF_8)
                        }

                        this.logger.log("")
                        if (isPlaintext(buffer)) {
                            this.logger.log(buffer.readString(charset!!))
                            this.logger.log("--> END " + request.method() + " (" + requestContentLength + "-byte body)")
                        } else {
                            this.logger.log("--> END " + request.method() + " (binary " + requestContentLength + "-byte body omitted)")
                        }
                    }
                } else {
                    this.logger.log("--> END " + request.method())
                }
            }

            val startNs = System.nanoTime()

            val response: Response
            try {
                response = chain.proceed(request)
            } catch (var25: IOException) {
                return null
            }

            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            val responseBody = response.body()
            val contentLength = responseBody!!.contentLength()
            val bodySize = if (contentLength != -1L) contentLength.toString() + "-byte" else "unknown-length"
            this.logger.log("<-- " + response.code() + ' '.toString() + response.message() + ' '.toString() + response.request().url() + " (" + tookMs + "ms" + (if (!logHeaders) ", $bodySize body" else "") + ')'.toString())
            if (logHeaders) {
                val headers = response.headers()
                var i = 0

                val count = headers.size()
                while (i < count) {
                    this.logger.log(headers.name(i) + ": " + headers.value(i))
                    ++i
                }

                if (logBody && HttpHeaders.hasBody(response)) {
                    if (this.bodyEncoded(response.headers())) {
                        this.logger.log("<-- END HTTP (encoded body omitted)")
                    } else {
                        val source = responseBody.source()
                        try {
                            source.request(9223372036854775807L)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            LogUtil.testError(" source.request(9223372036854775807L)----->error")
                        }

                        val buffer = source.buffer()
                        var charset: Charset? = UTF_8
                        val contentType = responseBody.contentType()
                        if (contentType != null) {
                            charset = contentType.charset(UTF_8)
                        }

                        if (!isPlaintext(buffer)) {
                            this.logger.log("")
                            this.logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                            return response
                        }

                        if (contentLength != 0L) {
                            this.logger.log("")
                            this.logger.log(buffer.clone().readString(charset!!))
                        }

                        this.logger.log("<-- END HTTP (" + buffer.size() + "-byte body)")
                    }
                } else {
                    this.logger.log("<-- END HTTP")
                }
            }

            return response
        }
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64L) buffer.size() else 64L
            buffer.copyTo(prefix, 0L, byteCount)

            var i = 0
            while (i < 16 && !prefix.exhausted()) {
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
                ++i
            }

            return true
        } catch (var6: EOFException) {
            return false
        }

    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    interface Logger {
        fun log(var1: String)
        companion object {
            //Platform.get().log(4, message, (Throwable) null);
            val DEFAULT = object : Logger {
                override fun log(var1: String) {
                    LogUtil.testInfo(var1)
                }
            }
        }
    }

    enum class Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }
}