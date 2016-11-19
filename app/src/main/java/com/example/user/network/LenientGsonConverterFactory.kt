package com.example.user.network

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit


/**
 * Created by user on 16. 11. 18.
 */
class LenientGsonConverterFactory private constructor(private val gson: Gson?) : Converter.Factory() {

    init {
        if (gson == null) throw NullPointerException("gson == null")
    }

    private inner class LenientGsonResponseBodyConverter<T> internal constructor(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
            val jsonReader = gson.newJsonReader(value.charStream())
            jsonReader.isLenient = true
            try {
                return adapter.read(jsonReader)
            } finally {
                value.close()
            }
        }
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<out kotlin.Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        if(gson == null) return null;
        val adapter = gson.getAdapter(TypeToken.get(type))
        if(adapter == null) return null
        return LenientGsonResponseBodyConverter(gson, adapter)

    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out kotlin.Annotation>?, methodAnnotations: Array<out kotlin.Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        if(gson == null) return null;
        val adapter = gson.getAdapter(TypeToken.get(type))
        if(adapter == null) return null
        return LenientGsonRequestBodyConverter(gson, adapter)
    }


    companion object {

        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        @JvmOverloads fun create(gson: Gson = Gson()): LenientGsonConverterFactory {
            return LenientGsonConverterFactory(gson)
        }
    }
}

/**
 * Create an instance using a default [Gson] instance for conversion. Encoding to JSON and
 * decoding from JSON (when no charset is specified by a header) will use UTF-8.
 */
internal class LenientGsonRequestBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        jsonWriter.isLenient = true
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }

    companion object {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }
}