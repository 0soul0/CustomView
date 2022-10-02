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

class PureWaterWave(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var viewWith by Delegates.notNull<Float>()
    private var viewHeight by Delegates.notNull<Float>()
    private var currentValue = 0f
    private var paint: Paint = Paint()
    private var unit by Delegates.notNull<Float>()
    private var circleCenterX by Delegates.notNull<Float>()
    private var circleCenterY by Delegates.notNull<Float>()
    private var radius by Delegates.notNull<Float>()

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
                currentValue = it.animatedValue as Float
                postInvalidate()
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
        //3.
        clipCircle(canvas)
        canvas?.translate(currentValue*4*unit,0f)
        //2.
        drawBackWave(canvas)
        canvas?.translate(-2*currentValue*4*unit,0f)
        //4.
        drawFrontWave(canvas)
        canvas?.translate(currentValue*4*unit,0f)
        //1.
        drawCircle(canvas)

    }

    private fun clipCircle(canvas: Canvas?){
        canvas?.clipPath(Path().apply {
            addCircle(
                circleCenterX,
                circleCenterY,
                radius,
                Path.Direction.CW
            )
        })
    }

    private fun drawCircle(canvas: Canvas?) {
        val gray = Color.GRAY

        canvas?.drawCircle(
            circleCenterX,
            circleCenterY,
            radius,
            paint.apply {
                color = gray
                style = Paint.Style.STROKE
                strokeWidth = unit / 8
            }
        )

    }

    private fun drawFrontWave(canvas: Canvas?) {
        // path(): https://blog.csdn.net/u012702547/article/details/52454406
        val altitude =circleCenterY-50
        val path = Path().apply {
            moveTo(-viewWith, circleCenterY)
        }

        (1..6).forEach {
            val x1 = (2 * it + -1) * unit
            val x2 = 2 * it * unit
            val y2 = altitude
            val y1 = altitude+if (it % 2 == 0) {
                 -unit/2
            } else {
                unit/2
            }
            path.quadTo(x1, y1, x2, y2)
        }

        path.lineTo(2*viewWith, altitude + radius+50)
        path.lineTo(0f, altitude + radius+50)

        path.close()

        canvas?.drawPath(path, paint.apply {
            color = Color.BLUE
            style = Paint.Style.FILL
        })

        path.reset()
    }

    private fun drawBackWave(canvas: Canvas?) {
        // path(): https://blog.csdn.net/u012702547/article/details/52454406
        val altitude =circleCenterY-90
        val path = Path().apply {
            moveTo(-viewWith, altitude)
        }

        (1..6).forEach {
            val x1 = -viewWith+(2 * it + -1) * unit
            val x2 = -viewWith+2 * it * unit
            val y2 = altitude
            val y1 =altitude + if (it % 2 == 1) {
                 -unit/2
            } else {
                unit/2
            }
            path.quadTo(x1, y1, x2, y2)
        }

        path.lineTo(viewWith, altitude + radius)
        path.lineTo(-viewWith, altitude + radius)

        path.close()

        canvas?.drawPath(path, paint.apply {
            color =Color.CYAN
            style = Paint.Style.FILL
        })

        path.reset()
    }


}