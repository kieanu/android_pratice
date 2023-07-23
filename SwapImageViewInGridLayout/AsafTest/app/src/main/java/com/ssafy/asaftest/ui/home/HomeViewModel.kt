package com.ssafy.asaftest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel : ViewModel() {
    private val _seat : MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val seat = _seat
}