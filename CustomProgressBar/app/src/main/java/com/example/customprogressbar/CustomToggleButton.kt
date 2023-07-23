package com.example.customprogressbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

class CustomToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val seatText: TextView
    val lockerText: TextView
    val moneyText : TextView
    val cardViewFocus : CardView

    init {
        inflate(context, R.layout.toggle_button, this)

        seatText = findViewById(R.id.seatText)
        lockerText = findViewById(R.id.lockerText)
        moneyText = findViewById(R.id.moneyText)
        cardViewFocus = findViewById(R.id.cardViewFocus)
    }
}