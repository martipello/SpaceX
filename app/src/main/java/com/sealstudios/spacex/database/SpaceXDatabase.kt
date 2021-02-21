package com.sealstudios.spacex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.paging.RemoteKeys


@Database(version = 1, entities = [LaunchResponse::class, RemoteKeys::class], exportSchema = false)
abstract class SpaceXDatabase : RoomDatabase() {

    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun launchDao(): LaunchDao

    companion object {

        private const val SPACE_X_DATABASE_NAME = "space_x.db"

        @Volatile
        private var INSTANCE: SpaceXDatabase? = null

        fun getInstance(context: Context): SpaceXDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SpaceXDatabase::class.java,
                SPACE_X_DATABASE_NAME
            ).build()
    }

}