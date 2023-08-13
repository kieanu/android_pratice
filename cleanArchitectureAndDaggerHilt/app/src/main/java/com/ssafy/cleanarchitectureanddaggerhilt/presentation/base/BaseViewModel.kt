package com.ssafy.cleanarchitectureanddaggerhilt.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.cleanarchitectureanddaggerhilt.presentation.utils.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel(private val contextProvider: CoroutineContextProvider) : ViewModel() {

    private val job: Job = Job()

    abstract val coroutineExceptionHandler: CoroutineExceptionHandler
    // CoroutineScope 대신 사용
    protected fun launchCoroutineIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(contextProvider.io + job + coroutineExceptionHandler) {
            block()
        }
    }

    protected fun launchCoroutineMain(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(contextProvider.main + job + coroutineExceptionHandler) {
            block()
        }
    }

    public override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
