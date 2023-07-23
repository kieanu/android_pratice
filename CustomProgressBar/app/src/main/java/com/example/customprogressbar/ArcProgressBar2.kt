package com.example.customprogressbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import app.futured.donut.DonutProgressView

class ArcProgressBar2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val progressText: TextView
    val progressRate: TextView
    val arcProgressBar : DonutProgressView

    init {
        // Inflate XML layout resource
        inflate(context, R.layout.arc_progress_bar, this)

        // Get references to the views within the custom layout
        progressText = findViewById(R.id.progressText)
        progressRate = findViewById(R.id.progressRate)
        arcProgressBar = findViewById(R.id.arcProgressBar)
        default(arcProgressBar)
    }

    fun default(arcView:DonutProgressView) {
        // 베경 컬러
        arcView!!.bgLineColor = Color.parseColor("#F9F5FF")

        // 시작 위치
        arcView!!.gapAngleDegrees = 90f
        arcView!!.gapWidthDegrees = 180f

        // max 값
        arcView!!.cap = 100f
        arcView!!.strokeWidth = 40f
    }
}