package com.example.drawsign

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate


class ApplicationClass : Application() {

    // 코틀린의 전역변수 문법
    companion object {
        const val SHARED_PREFERENCES_NAME = "SSAFY_APP"


        var dpHeight = 0.0F
        var dpWidth = 0.0F
    }

    override fun onCreate() {
        super.onCreate()

        val density = resources.displayMetrics.density

        dpHeight = resources.displayMetrics.heightPixels / density
        dpWidth = resources.displayMetrics.widthPixels / density
    }
}