package com.ssafy.livebroadcast.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.livebroadcast.ApplicationClass
import com.ssafy.livebroadcast.dto.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _rooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val rooms = _rooms

    init {
        loadRooms()
    }

    private fun loadRooms() {
        viewModelScope.launch {
            ApplicationClass.fireStore.getRoom().collect() {
                _rooms.emit(it)
            }
        }
    }
}