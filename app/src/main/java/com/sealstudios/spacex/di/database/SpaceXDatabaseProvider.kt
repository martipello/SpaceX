package com.sealstudios.spacex.di.database

import android.content.Context
import com.sealstudios.spacex.database.SpaceXDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SpaceXDatabaseProvider {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SpaceXDatabase {
        return SpaceXDatabase.getInstance(context)
    }

}