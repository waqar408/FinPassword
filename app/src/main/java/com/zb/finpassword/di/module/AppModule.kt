package com.zb.smartsaving.di.module

import android.content.Context
import androidx.databinding.library.BuildConfig
import com.nosugar.pref.PreferencesHelper
import com.nosugar.pref.PreferencesHelperImpl
import com.zb.finpassword.data.api.ApiService
import com.zb.finpassword.utils.Constant
import com.zb.finpassword.utils.NetworkHelper


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val appModule   = module {
    single<PreferencesHelper>{ providePrefHelper(androidContext())}
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), Constant.BASE_URL) }
    single { provideApiService(get()) }
    single { provideNetworkHelper(androidContext()) }


}
private fun provideNetworkHelper(context: Context) = NetworkHelper(context)
private fun providePrefHelper(context: Context) = PreferencesHelperImpl(context)

private fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpBuilder = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        okHttpBuilder.addInterceptor(loggingInterceptor)
    }

    return okHttpBuilder.build()
}
private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)