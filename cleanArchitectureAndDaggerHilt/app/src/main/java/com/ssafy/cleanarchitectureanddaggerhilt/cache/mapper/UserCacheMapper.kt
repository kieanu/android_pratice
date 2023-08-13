package com.ssafy.cleanarchitectureanddaggerhilt.cache.mapper

import com.ssafy.cleanarchitectureanddaggerhilt.cache.model.UserCacheEntity
import com.ssafy.cleanarchitectureanddaggerhilt.data.model.UserEntity
import javax.inject.Inject

class UserCacheMapper @Inject constructor() : CacheMapper<UserCacheEntity, UserEntity> {
    override fun mapFromCached(type: UserCacheEntity): UserEntity {
        return UserEntity(
            id = type.id,
            name = type.name,
            gender = type.gender,
            profileImage = type.profileImage
        )
    }

    override fun mapToCached(type: UserEntity): UserCacheEntity {
        return UserCacheEntity(
            id = type.id,
            name = type.name,
            gender = type.gender,
            profileImage = type.profileImage
        )
    }
}
