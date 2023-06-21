package com.ssafy.waveexample.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var _phoneNumber:String = "01012345678"
    val phoneNumber:String
        get() = _phoneNumber

    private var _message:String = "테스트 내용 입니다."
    val message:String
        get() = _message
}