package com.ssafy.cleanarchitectureanddaggerhilt.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssafy.cleanarchitectureanddaggerhilt.cache.dao.UserDao
import com.ssafy.cleanarchitectureanddaggerhilt.cache.util.CacheConstants
import javax.inject.Inject

@Database(
    entities = [UserDatabase::class],
    version = CacheConstants.DB_VERSION,
    exportSchema = false
)

abstract class UserDatabase: RoomDatabase() {

    abstract fun cachedUserDao(): UserDao // ROOM @Dao에 의해 자동 객체 주입(Hilt 와 무관)

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            CacheConstants.DB_NAME
        ).build()
    }
}