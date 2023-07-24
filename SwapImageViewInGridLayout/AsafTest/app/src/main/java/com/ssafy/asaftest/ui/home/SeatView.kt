package com.ssafy.asaftest.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.ssafy.asaftest.R

class SeatView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val seatText: TextView
    val seatImage: ImageView

    init {
        // Inflate XML layout resource
        inflate(context, R.layout.seatview, this)
        // Get references to the views within the custom layout
        seatText = findViewById(R.id.item_seat_textview_text)
        seatImage = findViewById(R.id.item_seat_imageview_image)
    }

}
