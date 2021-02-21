package com.sealstudios.spacex.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sealstudios.spacex.objects.LaunchResponse

@Dao
interface LaunchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(launches: List<LaunchResponse>)

    @Query("SELECT * FROM LaunchResponse")
    fun getAllLaunches(): PagingSource<Int, LaunchResponse>

    @Query("DELETE FROM LaunchResponse")
    suspend fun clearAllLaunches()

}