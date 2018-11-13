package com.stephen.mvpframework.network;

import android.support.annotation.NonNull;


import com.stephen.mvpframework.utils.LogUtil;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 日志拦截器
 * Created by Stephen on 2017/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final Logger logger;
    private volatile Level level;

     HttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

     private HttpLoggingInterceptor(Logger logger) {
        this.level = Level.NONE;
        this.logger = logger;
    }

     HttpLoggingInterceptor setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        } else {
            this.level = level;
            return this;
        }
    }

    public Level getLevel() {
        return this.level;
    }

    public Response intercept(@NonNull Chain chain) {
        Level level = this.level;
        Request request = chain.request();
        if (level == Level.NONE) {

            try {
                return chain.proceed(request);
            } catch (IOException e) {
                return null;
            }
        } else {
            boolean logBody = level == Level.BODY;
            boolean logHeaders = logBody || level == Level.HEADERS;
            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;
            Connection connection = chain.connection();
            Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
            String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
            long requestContentLength;
            try {
                requestContentLength = requestBody.contentLength();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                requestContentLength = -1;
            }
            if (!logHeaders && hasRequestBody) {
                requestStartMessage = requestStartMessage + " (" + requestContentLength + "-byte body)";
            }

            this.logger.log(requestStartMessage);
            if (logHeaders) {
                if (hasRequestBody) {
                    if (requestBody.contentType() != null) {
                        this.logger.log("Content-Type: " + requestBody.contentType());
                    }

                    if (requestContentLength != -1L) {
                        this.logger.log("Content-Length: " + requestContentLength);
                    }
                }

                Headers headers = request.headers();
                int i = 0;

                for (int count = headers.size(); i < count; ++i) {
                    String name = headers.name(i);
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        this.logger.log(name + ": " + headers.value(i));
                    }
                }

                if (logBody && hasRequestBody) {
                    if (this.bodyEncoded(request.headers())) {
                        this.logger.log("--> END " + request.method() + " (encoded body omitted)");
                    } else {
                        Buffer buffer = new Buffer();
                        try {
                            requestBody.writeTo(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtil.testError("requestBody.writeTo(buffer)---->error");
                        }
                        Charset charset = UTF8;
                        MediaType contentType = requestBody.contentType();
                        if (contentType != null) {
                            charset = contentType.charset(UTF8);
                        }

                        this.logger.log("");
                        if (isPlaintext(buffer)) {
                            this.logger.log(buffer.readString(charset));
                            this.logger.log("--> END " + request.method() + " (" + requestContentLength + "-byte body)");
                        } else {
                            this.logger.log("--> END " + request.method() + " (binary " + requestContentLength + "-byte body omitted)");
                        }
                    }
                } else {
                    this.logger.log("--> END " + request.method());
                }
            }

            long startNs = System.nanoTime();

            Response response;
            try {
                response = chain.proceed(request);
            } catch (IOException var25) {
                return null;
            }

            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1L ? contentLength + "-byte" : "unknown-length";
            this.logger.log("<-- " + response.code() + ' ' + response.message() + ' ' + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');
            if (logHeaders) {
                Headers headers = response.headers();
                int i = 0;

                for (int count = headers.size(); i < count; ++i) {
                    this.logger.log(headers.name(i) + ": " + headers.value(i));
                }

                if (logBody && HttpHeaders.hasBody(response)) {
                    if (this.bodyEncoded(response.headers())) {
                        this.logger.log("<-- END HTTP (encoded body omitted)");
                    } else {
                        BufferedSource source = responseBody.source();
                        try {
                            source.request(9223372036854775807L);
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtil.testError(" source.request(9223372036854775807L)----->error");
                        }
                        Buffer buffer = source.buffer();
                        Charset charset = UTF8;
                        MediaType contentType = responseBody.contentType();
                        if (contentType != null) {
                            charset = contentType.charset(UTF8);
                        }

                        if (!isPlaintext(buffer)) {
                            this.logger.log("");
                            this.logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                            return response;
                        }

                        if (contentLength != 0L) {
                            this.logger.log("");
                            this.logger.log(buffer.clone().readString(charset));
                        }

                        this.logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
                    }
                } else {
                    this.logger.log("<-- END HTTP");
                }
            }

            return response;
        }
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64L ? buffer.size() : 64L;
            buffer.copyTo(prefix, 0L, byteCount);

            for (int i = 0; i < 16 && !prefix.exhausted(); ++i) {
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public interface Logger {
        //Platform.get().log(4, message, (Throwable) null);
        Logger DEFAULT = LogUtil::testInfo;

        void log(String var1);
    }

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY;

        Level() {
        }
    }
}