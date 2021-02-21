package com.sealstudios.spacex.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sealstudios.spacex.database.SpaceXDatabase
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchResponse
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class LaunchResponseMediator(
    private val spaceXService: SpaceXService,
    private val spaceXDatabase: SpaceXDatabase,
    private val launchQueryData: LaunchQueryData
) : RemoteMediator<Int, LaunchResponse>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, LaunchResponse>): MediatorResult {
        Log.d("MEDIATOR_LOG", "load - state is $state")
        val page = when (loadType) {
            LoadType.REFRESH -> {
                Log.d("MEDIATOR_LOG", "LoadType.REFRESH")
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Log.d("MEDIATOR_LOG", "LoadType.PREPEND")
                // The LoadType is PREPEND so some data was loaded before,
                // so we should have been able to get remote keys
                // If the remoteKeys are null, then we're an invalid state and we have a bug
                val remoteKeys = getFirstRemoteKey(state) ?: return MediatorResult.Error(InvalidObjectException("PREPEND Remote key and the prevKey should not be null"))
                // If the previous key is null, then we can't request more data
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)

                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                Log.d("MEDIATOR_LOG", "LoadType.APPEND")
                val remoteKeys = getLastRemoteKey(state)
                if (remoteKeys?.nextKey == null) {
                    return MediatorResult.Error(InvalidObjectException("APPEND Remote key and the prevKey should not be null"))
                }
                remoteKeys.nextKey
            }

        }

        try {
            val apiResponse = spaceXService.queryLaunches(
                launchQueryData.apply {
                    this.options?.page = page
//                this.options?.populate = listOf("rocket")
                })

            val launchResponse = apiResponse.docs
            val endOfPaginationReached = launchResponse.isEmpty()

            spaceXDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    spaceXDatabase.remoteKeysDao().clearRemoteKeys()
                    spaceXDatabase.launchDao().clearAllLaunches()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = launchResponse.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                spaceXDatabase.remoteKeysDao().insertAll(keys)
                spaceXDatabase.launchDao().insertAll(launchResponse)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, LaunchResponse>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                remoteKeys.prevKey ?: MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }

    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, LaunchResponse>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { launch ->
                Log.d("MEDIATOR_LOG", "FIRST $launch")
                spaceXDatabase.remoteKeysDao().remoteKeysLaunchResponseId(launch.id)
            }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, LaunchResponse>): RemoteKeys? {
        Log.d("MEDIATOR_LOG", "getLastRemoteKey ${state.pages}")
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { launch ->
                Log.d("MEDIATOR_LOG", "LAUNCH IS $launch")
                val key = spaceXDatabase.remoteKeysDao().remoteKeysLaunchResponseId(launch.id)
                Log.d("MEDIATOR_LOG", "KEY IS $key")
                return@let key
            }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, LaunchResponse>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { remoteKeyId ->
                spaceXDatabase.remoteKeysDao().remoteKeysLaunchResponseId(remoteKeyId)
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }
}