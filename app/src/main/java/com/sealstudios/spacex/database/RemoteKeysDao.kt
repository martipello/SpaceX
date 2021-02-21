package com.sealstudios.spacex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sealstudios.spacex.paging.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE id = :id")
    suspend fun remoteKeysLaunchResponseId(id: String): RemoteKeys?

    @Query("DELETE FROM RemoteKeys")
    suspend fun clearRemoteKeys()
}