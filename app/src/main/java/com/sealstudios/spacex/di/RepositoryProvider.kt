package com.sealstudios.spacex.di

import com.sealstudios.spacex.database.SpaceXDatabase
import com.sealstudios.spacex.network.ResponseHandler
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.repositories.SpaceXRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryProvider {
    @Singleton
    @Provides
    fun provideSpaceXRepository(
        spaceXDatabase: SpaceXDatabase,
        spaceXService: SpaceXService,
        responseHandler: ResponseHandler
    ): SpaceXRepository {
        return SpaceXRepository(
            spaceXService = spaceXService,
            spaceXDatabase = spaceXDatabase,
            responseHandler = responseHandler
        )
    }
}