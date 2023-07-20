package com.example.customprogressbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import app.futured.donut.DonutProgressView

class ArcProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val arcProgressBar: DonutProgressView

    init {
        // FinalCustomView를 인플레이트하여 추가
        arcProgressBar = DonutProgressView(context)
        addView(arcProgressBar)
        default(arcProgressBar)
    }

    private fun default(arcView:DonutProgressView) {
        // 베경 컬러
        arcView!!.bgLineColor = Color.parseColor("#F9F5FF")

        // 시작 위치
        arcView!!.gapAngleDegrees = 90f
        arcView!!.gapWidthDegrees = 180f

        // max 값
        arcView!!.cap = 100f
        arcView!!.strokeWidth = 32f
    }
}
