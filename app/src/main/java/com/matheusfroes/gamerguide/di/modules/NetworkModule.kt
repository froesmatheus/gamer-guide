package com.matheusfroes.gamerguide.di.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

@Module()
class NetworkModule {

    @Provides
    fun gson(): Gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, object : TypeAdapter<Date>() {
                override fun write(writer: JsonWriter, value: Date) {
                    writer.value(value.time)
                }

                override fun read(reader: JsonReader): Date {
                    return Date(reader.nextLong())
                }
            }.nullSafe())
            .create()

    @Provides
    fun gsonConverter(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    fun rxJavaAdapter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor.Logger { Timber.d(it) }

        return HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun okHttpClient(
            loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    fun providePicasso(
            context: Context
    ): Picasso = Picasso.Builder(context)
            .indicatorsEnabled(true)
            .build()

}
