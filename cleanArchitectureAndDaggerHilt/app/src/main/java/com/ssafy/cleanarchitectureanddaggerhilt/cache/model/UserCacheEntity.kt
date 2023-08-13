package com.ssafy.cleanarchitectureanddaggerhilt.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssafy.cleanarchitectureanddaggerhilt.cache.util.CacheConstants

@Entity(tableName = CacheConstants.USER_TABLE_NAME)
data class UserCacheEntity(
    @PrimaryKey(autoGenerate = true) // 기본이 true
    val id: Int,
    val name: String,
    val gender: String,
    val profileImage: String // 이미지 링크 (Glide)
)
