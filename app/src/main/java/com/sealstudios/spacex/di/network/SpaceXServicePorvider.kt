package com.sealstudios.spacex.di.network

import com.sealstudios.spacex.BuildConfig
import com.sealstudios.spacex.network.OkHttpClient
import com.sealstudios.spacex.network.RetrofitSpaceXService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SpaceXServiceProvider {

    @Provides
    @Singleton
    fun provideSpaceXService(okHttpClient: OkHttpClient): RetrofitSpaceXService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.getOkHttpClient())
                .build()
                .create(RetrofitSpaceXService::class.java)
    }
}
