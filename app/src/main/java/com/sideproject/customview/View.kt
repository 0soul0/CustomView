package com.sideproject.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.green
import kotlin.properties.Delegates

class View(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var viewWith by Delegates.notNull<Float>()
    private var viewHeight by Delegates.notNull<Float>()
    private var unit by Delegates.notNull<Float>()
    private var circleCenterX by Delegates.notNull<Float>()
    private var circleCenterY by Delegates.notNull<Float>()
    private var radius by Delegates.notNull<Float>()
    private var currentValue = 0f
    private var paint: Paint = Paint()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        animator()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        viewWith = w.toFloat()

        val minh: Int = MeasureSpec.getSize(w) - rootView.width + paddingBottom + paddingTop
        val h: Int = resolveSizeAndState(minh, heightMeasureSpec, 0)
        viewHeight = h.toFloat()
        setMeasuredDimension(w, h)
    }

    private fun animator() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.apply {
            duration = 1000
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
//                currentValue = it.animatedValue as Float
//                postInvalidate()
            }
            start()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        unit = viewWith / 6
        circleCenterX = viewWith / 2
        circleCenterY = viewHeight / 2
        radius = viewWith / 3

    }
}