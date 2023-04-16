package com.example.phinconattendance.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.phinconattendance.R

class CheckInOutButton : AppCompatButton {
    private lateinit var activatedBackground: Drawable
    private lateinit var inactivatedBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isActivated) activatedBackground else inactivatedBackground
        setTextColor(txtColor)
        textSize = 30f
        gravity = Gravity.CENTER
        text = if (isActivated) "CHECK IN" else "CHECK OUT"
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        activatedBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_check_in) as Drawable
        inactivatedBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_button_check_out) as Drawable
    }
}