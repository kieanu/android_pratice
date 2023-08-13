package com.ssafy.cleanarchitectureanddaggerhilt.data.repository

import com.ssafy.cleanarchitectureanddaggerhilt.data.model.UserEntity

interface UserDataSource {
    // Remote and cache  (원격, 로컬 둘다 공통으로 사용하는 메서드)
    suspend fun getUsers(): List<UserEntity>
    suspend fun getUser(characterId: Long): UserEntity

    // Cache (로컬에서만 사용하는 메서드)
    suspend fun saveUsers(listUsers: List<UserEntity>)
    suspend fun deleteUsers(listUsers: List<UserEntity>)
    suspend fun updateUsers(listUsers: List<UserEntity>)
    suspend fun isCached(): Boolean
}
