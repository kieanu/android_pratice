package com.ssafy.cleanarchitectureanddaggerhilt.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ssafy.cleanarchitectureanddaggerhilt.cache.model.UserCacheEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getUsers(): List<UserCacheEntity>

    @Query("SELECT * FROM users WHERE  id = :id")
    fun getUser(id: Long): UserCacheEntity

    @Query("DELETE FROM users")
    fun clearUsers(): Int

    @Delete
    fun clearUser(vararg user: UserCacheEntity): Int

    @Update
    fun updateUser(vararg user: UserCacheEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(vararg user: UserCacheEntity)
}
