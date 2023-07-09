package com.ssafy.livebroadcast

import android.app.Application
import com.ssafy.livebroadcast.repository.remote.Remote

class ApplicationClass : Application() {
    companion object {
        val fireStore by lazy {
            Remote()
        }
    }
}