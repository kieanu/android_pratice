package com.ssafy.cleanarchitectureanddaggerhilt.domain.repository

import com.ssafy.cleanarchitectureanddaggerhilt.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // Remote and cache
    suspend fun getUsers(): Flow<List<User>>
    suspend fun getUser(characterId: Long): Flow<User>

    // Cache -> UI를 변경하는 작업이 아닌 CacheDB를 건드는 작업이므로 Flow 타입이 아님
    suspend fun saveUsers(listUsers: List<User>)
    suspend fun deleteUsers(listUsers: List<User>)
    suspend fun updateUsers(listUsers: List<User>)
}
