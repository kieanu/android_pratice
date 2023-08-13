package com.ssafy.cleanarchitectureanddaggerhilt.di.module

import android.content.Context
import com.ssafy.cleanarchitectureanddaggerhilt.cache.dao.UserDao
import com.ssafy.cleanarchitectureanddaggerhilt.cache.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.cachedUserDao()
    }

//    @Provides
//    @Singleton
//    fun provideCharacterCache(characterCache: CharacterCacheImp): CharacterCache {
//        return characterCache
//    }
}
