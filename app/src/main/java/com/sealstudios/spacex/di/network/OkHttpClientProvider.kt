package com.sealstudios.spacex.di.network

import com.sealstudios.spacex.network.NetworkConnectionInterceptor
import com.sealstudios.spacex.network.OkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class OkHttpClientProvider {

    @InterceptorOkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor): OkHttpClient {
        return OkHttpClient(networkConnectionInterceptor)
    }

    @TestInterceptorOkHttpClient
    @Singleton
    @Provides
    fun provideTestOkHttpClient(testInterceptor: NetworkConnectionInterceptor): OkHttpClient {
        return OkHttpClient(testInterceptor)
    }
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TestInterceptorOkHttpClient