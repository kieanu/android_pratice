package com.ssafy.cleanarchitectureanddaggerhilt.data.source

import com.ssafy.cleanarchitectureanddaggerhilt.data.repository.UserCache
import com.ssafy.cleanarchitectureanddaggerhilt.data.repository.UserDataSource
import javax.inject.Inject

open class UserDataSourceFactory @Inject constructor(
    // private val userCache: UserCache,
    private val cacheDataSource: UserCacheDataSource,
    // private val remoteDataSource: CharacterRemoteDataSource
) {

    open suspend fun getDataStore(isCached: Boolean): UserDataSource {
        return if (isCached) {
            return getCacheDataSource()
        } else {getCacheDataSource()}
//        else {
//            getRemoteDataSource()
//        }
    }

//    fun getRemoteDataSource(): CharacterDataSource {
//        return remoteDataSource
//    }

    fun getCacheDataSource(): UserDataSource {
        return cacheDataSource
    }
}
