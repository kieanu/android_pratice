package com.ssafy.cleanarchitectureanddaggerhilt.data.source

import android.service.autofill.UserData
import com.ssafy.cleanarchitectureanddaggerhilt.data.model.UserEntity
import com.ssafy.cleanarchitectureanddaggerhilt.data.repository.UserCache
import com.ssafy.cleanarchitectureanddaggerhilt.data.repository.UserDataSource
import javax.inject.Inject

class UserCacheDataSource @Inject constructor(
    private val userCache: UserCache
) : UserDataSource {

    override suspend fun getUsers(): List<UserEntity> {
        return userCache.getUsers()
    }

    override suspend fun getUser(characterId: Long): UserEntity {
        return userCache.getUser(characterId)
    }

    override suspend fun saveUsers(listUsers: List<UserEntity>) {
        return userCache.saveUsers(listUsers)
    }

    override suspend fun deleteUsers(listUsers: List<UserEntity>) {
        return userCache.deleteUsers(listUsers)
    }

    override suspend fun updateUsers(listUsers: List<UserEntity>) {
        return userCache.updateUsers(listUsers)
    }

    override suspend fun isCached(): Boolean {
        return userCache.isCached()
    }
}
