package com.picpay.desafio.android.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.URL_BASE_SERVICE
import com.picpay.desafio.android.framework.network.PicPayService
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

val networkModule = module {
    factory { provideGson() }
    factory { provideConverterFactory(get())}
    factory { provideOkHttpClient() }
    single { provideRetrofit(get(), get()) }
    factory { providePicPayApi(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
    return Retrofit.Builder().baseUrl(URL_BASE_SERVICE).client(okHttpClient)
        .addConverterFactory(gsonConverterFactory).build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder()
        .addInterceptor(Interceptor { chain ->

        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code / 100) {

            2 -> {
                return@Interceptor response
            }
            else -> {

                val exception: Exception?

                try{

                    val responseMessage = response.use { it.body?.byteStream()?.reader().use { reader -> reader?.readText() } }
                    exception = Exception("code " + response.code + responseMessage?: "")

                } catch (exception: Exception){
                    exception.printStackTrace()
                    return@Interceptor response
                }

                throw exception

            }
        }
    })
    .build()
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}

fun provideConverterFactory(gson: Gson): GsonConverterFactory {
    return GsonConverterFactory.create(gson)
}

fun providePicPayApi(retrofit: Retrofit): PicPayService = retrofit.create(PicPayService::class.java)
