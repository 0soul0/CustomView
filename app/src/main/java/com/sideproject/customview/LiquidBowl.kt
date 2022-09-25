package com.sideproject.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import kotlin.properties.Delegates

class LiquidBowl(context: Context,attrs: AttributeSet?):View(context,attrs){

    private var viewWith by Delegates.notNull<Float>()
    private var viewHeight by Delegates.notNull<Float>()
    private var currentValue = 0f

    init {
        setLayerType(LAYER_TYPE_SOFTWARE,null)
        animator()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft+paddingRight+suggestedMinimumWidth
        val w:Int = resolveSizeAndState(minw,widthMeasureSpec,1)
        viewWith = w.toFloat()

        val minh:Int = MeasureSpec.getSize(w)-rootView.width+paddingBottom+paddingTop
        val h:Int = resolveSizeAndState(minh,heightMeasureSpec,0)
        viewHeight=h.toFloat()
        setMeasuredDimension(w,h)
    }

    private fun animator(){
        val valueAnimator = ValueAnimator.ofFloat(-1f,1f)
        valueAnimator.apply {
            duration=2000
            repeatMode=ValueAnimator.REVERSE
            repeatCount=ValueAnimator.INFINITE
            interpolator=LinearInterpolator()
            addUpdateListener {
                currentValue = it.animatedValue as Float
                Log.d("testtest", "animator: $currentValue")
                invalidate()
            }
            start()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bowl(canvas)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bowl(canvas: Canvas?) {
        val gray = Color.GRAY
        val dkGray = Color.DKGRAY
        val ltGray = Color.LTGRAY
        val transparentDray = Color.argb(50,220,220,220)
        val waterColor = Color.rgb(0f,190f+currentValue,254f-currentValue)

        val radius = viewWith/4
        val circleCenterX = viewWith/2
        val circleCenterY = viewHeight/2
        val bowlBottomX = viewWith/2
        val bowlBottomY = viewHeight/2+radius
        val rotateDegrees =currentValue*20
        val unit = radius/12
        val paint = Paint()

        canvas?.apply{

            // bowl shadow
            paint.color=dkGray
            paint.style=Paint.Style.FILL
            drawOval(circleCenterX-11*unit,circleCenterY+11*unit,
                circleCenterX+11*unit,circleCenterY+13*unit,paint)

            rotate(rotateDegrees,bowlBottomX,bowlBottomY)


            // bowl
            paint.color = gray
            paint.style=Paint.Style.FILL
            drawCircle(circleCenterX,circleCenterY,radius,paint)

            // mouth of bowl
            paint.color =ltGray
            paint.strokeWidth=30f
            paint.style=Paint.Style.STROKE
            paint.setShadowLayer(20.0f,0.0f,20.0f,Color.DKGRAY)
            drawOval(circleCenterX-6*unit,circleCenterY-12*unit,
                circleCenterX+6*unit,circleCenterY-8*unit,paint)


            rotate(-rotateDegrees,circleCenterX,circleCenterY)

            // liquid
            paint.color=waterColor
            paint.style=Paint.Style.FILL
            paint.setShadowLayer(500.0f,0.0f,0f,waterColor)
            val oval =RectF(circleCenterX-11*unit,circleCenterY-radius
            ,circleCenterX+11*unit,circleCenterY+11*unit)
            drawArc(oval,0f,180f,true,paint)

            // liquid surface
            paint.color=waterColor
            paint.style=Paint.Style.FILL
            drawOval(circleCenterX-11*unit,circleCenterY-unit-10,circleCenterX+11*unit,
            circleCenterY+unit-10,paint)


            rotate(rotateDegrees,circleCenterX,circleCenterY)

            //reflective
            paint.color=transparentDray
            paint.strokeWidth=15f
            paint.style=Paint.Style.FILL
            paint.clearShadowLayer()
            drawOval(circleCenterX-6*unit,circleCenterY-6*unit,
                circleCenterX+6*unit,circleCenterY,paint)


        }




    }


}