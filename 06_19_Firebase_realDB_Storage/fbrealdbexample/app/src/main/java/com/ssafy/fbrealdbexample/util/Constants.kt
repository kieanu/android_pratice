package com.ssafy.flowexample1.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ssafy.fbrealdbexample.R

object Constants {
    fun setImage(context: Context, view: ImageView, url: String) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background)
            .into(view)
    }
}