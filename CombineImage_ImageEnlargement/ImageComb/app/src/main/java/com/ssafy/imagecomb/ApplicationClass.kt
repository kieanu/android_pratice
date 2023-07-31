package com.ssafy.imagecomb

import android.app.Application
import android.content.Context
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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        dpHeight = outMetrics.heightPixels / density
        dpWidth = outMetrics.widthPixels / density
    }
}