package com.ssafy.cleanarchitectureanddaggerhilt.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.cleanarchitectureanddaggerhilt.presentation.base.BaseViewModel
import com.ssafy.cleanarchitectureanddaggerhilt.presentation.utils.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
) : BaseViewModel(contextProvider) {
    // scope.launch(exceoptionHandler) << 비동기 에러 처리 가능
    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("홈뷰모델", "$exception")
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}