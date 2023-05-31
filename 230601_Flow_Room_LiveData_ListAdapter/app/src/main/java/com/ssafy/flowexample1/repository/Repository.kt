package com.ssafy.flowexample1.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.flowexample1.database.UserDao
import com.ssafy.flowexample1.database.UserDatabase
import com.ssafy.flowexample1.database.UserDto
import kotlinx.coroutines.flow.*

private const val DATABASE_NAME = "user-db.db"
class Repository(context: Context) {
    private val userDB : UserDatabase = Room.databaseBuilder(
        context.applicationContext,
        UserDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val userDao:UserDao = userDB.userDao()
    // 이렇게 해놓으면 데이터소스(ROOM DB가 변할 때마다 getUsers() 함수-Flow가 자동으로 호출되는 듯?
    fun getUsers() : Flow<List<UserDto>> {
        return userDao.getUsers()
    }

    // Flow<Unit> -> emit 완료를 알리기 위함
    // flow도 코루틴 영역(비동기) suspend없이도 withTransaction을 flow{}내에서 사용가능함
    fun insertUser(user : UserDto) : Flow<Unit> {
        return channelFlow {
            userDB.withTransaction {
                userDao.insertUser(user)
                send(Unit)
            }
        }
    }

    fun updateUser(user : UserDto) : Flow<Unit> {
        return channelFlow {
            userDB.withTransaction {
                userDao.updateUser(user)
                send(Unit)
            }
        }
    }

    fun deleteUser(user : UserDto) : Flow<Unit> {
        return channelFlow {
            userDB.withTransaction {
                userDao.deleteUser(user)
                send(Unit)
            }
        }
    }
}