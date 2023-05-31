package com.ssafy.flowexample1.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.flowexample1.ApplicationClass
import com.ssafy.flowexample1.database.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
// https://velog.io/@201/LiveData%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-UI%EC%99%80-Data%EB%A5%BC-%EC%97%B0%EA%B2%B0%ED%95%98%EA%B8%B0
class UserViewModel : ViewModel() {
    private val _userList= MutableLiveData<MutableList<UserDto>>()
    val userList: LiveData<MutableList<UserDto>>
        get() = _userList

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch {
            ApplicationClass.repository.getUsers()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    _userList.value = it.toMutableList()
                    Log.d("싸피", "getUsers 라이브데이터 성공: ${userList.value}")
                }
        }
    }

    fun addUser(user : UserDto) {
        // 라이브 데이터 갱신은 객체주소를 바꿔야 함
        // _userList.value = _userList.value?.also{ it.add(user) }
        // DB 갱신은 비동기로
        viewModelScope.launch {
            ApplicationClass.repository.insertUser(user)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    Log.d("싸피 인서트", "addUser 새로 등록됨: ${_userList.value}")
                }
        }
    }

    fun deleteUser(user : UserDto) {
        Log.d("싸피 삭제 대상", "deleteUser: $user")
        viewModelScope.launch {
            ApplicationClass.repository.deleteUser(user)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    Log.d("싸피 딜리트", "delUser 삭제됨: ${_userList.value}")
                }
        }
    }

    fun updateUser(user: UserDto) {
        viewModelScope.launch {
            ApplicationClass.repository.updateUser(user)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    Log.d("싸피 업데이트", "User 업뎃됨: ${_userList.value}")
                }
        }
    }
}