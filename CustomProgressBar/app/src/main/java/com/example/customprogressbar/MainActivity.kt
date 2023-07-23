package com.example.customprogressbar

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val donut_view = findViewById<DonutProgressView>(R.id.donut_view)
//        // 베경 컬러
//        donut_view.bgLineColor = Color.parseColor("#F9F5FF")
//
//        val section1 = DonutSection(
//            name = "section_1",
//            color = Color.parseColor("#1C58D9"),
//            amount = 40f
//        )
//
//        // max 값
//        donut_view.cap = 100f
//        donut_view.submitData(listOf(section1))
    }
}