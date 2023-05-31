package com.ssafy.flowexample1.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDto::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}