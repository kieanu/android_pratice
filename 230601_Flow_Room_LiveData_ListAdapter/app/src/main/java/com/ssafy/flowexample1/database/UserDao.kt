package com.ssafy.flowexample1.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    // List<UserDto>를 return 하고 Repository에서 send(getUsers())를 하면 DB 변경사항이 반영이 안됨 왜그럼?
    fun getUsers() : Flow<List<UserDto>>

    @Query("SELECT * FROM user WHERE id = (:id)")
    fun getUser(id: Long) : UserDto

    // 충돌시 에러
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(dto : UserDto)

    @Update
    suspend fun updateUser(dto : UserDto)

    @Delete
    suspend fun deleteUser(dto : UserDto)
}