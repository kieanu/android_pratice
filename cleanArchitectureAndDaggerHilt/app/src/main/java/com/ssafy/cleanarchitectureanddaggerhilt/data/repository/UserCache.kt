package com.ssafy.cleanarchitectureanddaggerhilt.data.repository

import com.ssafy.cleanarchitectureanddaggerhilt.data.model.UserEntity

interface UserCache {
    suspend fun getUsers(): List<UserEntity>
    suspend fun getUser(characterId: Long): UserEntity
    suspend fun saveUsers(listCharacters: List<UserEntity>)
    suspend fun deleteUsers(listCharacters: List<UserEntity>)
    suspend fun updateUsers(listCharacters: List<UserEntity>)
    suspend fun isCached(): Boolean
}