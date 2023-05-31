package com.ssafy.flowexample1.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDto(
    @ColumnInfo(name = "name") val name: String?, // 이름
    @ColumnInfo(name = "age") val age: Int?, // 나이
    @ColumnInfo(name = "introduce") val introduce: String?, // 자기소개
    @ColumnInfo(name = "avatar") val avatar: String //  image http
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    constructor(id:Long, name:String?, age: Int?, introduce: String?, avatar: String): this(name, age, introduce, avatar){
        this.id = id
    }

    override fun toString(): String {
        return "$name $age $introduce"
    }
}