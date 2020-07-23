package com.luis.lib

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.*
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

class AnalogWatch(context: Context, attr: AttributeSet): View(context, attr) {

    private var seconds: Double = 0.0
    var colorShapeTime = Color.BLACK
        set(value) {
            if (this::paintShapeTime.isInitialized) paintShapeTime.color = value
            field = value
        }
    var colorShapeCircle = Color.RED
        set(value) {
            if (this::paintShapeCircle.isInitialized) paintShapeCircle.color = value
            field = value
        }
    var colorTime = Color.BLACK
        set(value) {
            if (this::paintTime.isInitialized) paintTime.color = value
            field = value
        }
    var colorAmPm = Color.DKGRAY
        set(value) {
            if (this::paintAmPmTime.isInitialized) paintAmPmTime.color = value
            field = value
        }
    var is24Hours = false
    private lateinit var paintShapeTime: Paint
    private lateinit var paintShapeCircle: Paint
    private lateinit var paintAmPmTime: Paint
    private lateinit var paintTime: Paint
    private lateinit var shapes: List<Line>
    private var centerX by Delegates.notNull<Float>()
    private var centerY by Delegates.notNull<Float>()
    private var arcRadius by Delegates.notNull<Float>()
    private var conTime: Rect = Rect()
    private var contextAmPm: Rect = Rect()
    var sizeTime = 130f
    var sizeAmPmTime = 50f
    private var circleX = 0f
    private var circleY = 0f
    private var timeX = 0f
    private var timeY = 0f
    private var amPmX = 0f
    private var amPmY = 0f
    private var hours = "00"
    private var minutes = "00"
    private var am_pm = "EMPTY"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = DEFAULT_SIZE
        var height = DEFAULT_SIZE

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(width, widthSize)
            else ->  width
        }
        height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(height, heightSize)
            else -> height
        }
        val min = min(width, height)

        centerX = width * 0.5f
        centerY = height * 0.5f

        arcRadius = (min / 2f) - 60

        initShapes()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        measureTimeMillis { configure()}
        canvas?.apply {
            if(!is24Hours) drawText(am_pm, amPmX, amPmY, paintAmPmTime)
            drawText("$hours:$minutes", timeX, timeY, paintTime)
            drawCircle(circleX, circleY, 20f, paintShapeCircle)
            shapes.forEach{ (left, top, right, bottom) ->
                drawLine(left, top, right, bottom, paintShapeTime)
            }
        }
        postInvalidate()
    }

    private fun initShapes() {
        shapes = create()
    }

    private fun configure(time: Double = seconds) {
        getTime()
        circle(timeToAngle(time))
        timePosition()
        amPmPosition()
        shapes.mapIndexed { index, line ->
            val trueIndex = index * 3
            shapeTime(trueIndex, line::set)
        }
    }

    private fun amPmPosition(){
        paintAmPmTime.getTextBounds(am_pm, 0 , am_pm.length, contextAmPm)
        amPmX = centerX - (contextAmPm.width() / 2)
        amPmY = centerY - ((paintAmPmTime.descent() - paintAmPmTime.ascent() / 2) - sizeTime)
    }

    private fun getTime() {
        val c = Calendar.getInstance()
        hours = if(!is24Hours) c.get(Calendar.HOUR).toString()
        else c.get(Calendar.HOUR_OF_DAY).toString()
        minutes = if(c.get(Calendar.MINUTE) < 10) "0${c.get(Calendar.MINUTE)}"
        else "${c.get( Calendar.MINUTE)}"
        if (!is24Hours) am_pm = if (c.get(Calendar.AM_PM) == 0) "AM" else "PM"
        seconds = c.get(Calendar.SECOND).toDouble() + (c.get(Calendar.MILLISECOND).toDouble() * 0.001)
    }

    private inline fun shapeTime(position: Int, shape: (Float, Float, Float, Float) -> Unit) {
        val ext =  endTime( position / 3, seconds * 2.0)
        shape(
            centerX - (arcRadius * cos(Math.toRadians(position.toDouble() + ANGLE_OFF_SET).toFloat())),
            centerY - (arcRadius * sin(Math.toRadians(position.toDouble() + ANGLE_OFF_SET).toFloat())),
            centerX - (((arcRadius + RAD_END_SHAPE + ext) ) * cos(Math.toRadians(position.toDouble() + ANGLE_OFF_SET).toFloat())),
            centerY - (((arcRadius + RAD_END_SHAPE + ext) ) * sin(Math.toRadians(position.toDouble() + ANGLE_OFF_SET).toFloat()))
        )
    }

    private fun endTime(position: Int, seconds: Double): Float {
        val rad = (arcRadius + RAD_END_SHAPE).toDouble()
        var realGrade = seconds - position
        if (realGrade > 60.0) realGrade -= 120.0
        else if (realGrade < -60.0) realGrade += 120
        val result = sqrt((rad.pow(2)*2)-((2*rad*rad)*cos(Math.toRadians(realGrade))))
        return if (result < 30.0) (30.0 - result).toFloat() else 0f
    }

    private fun circle(time: Double) {
        circleX = centerX - ((arcRadius - 50) * cos(Math.toRadians(time + ANGLE_OFF_SET).toFloat()))
        circleY = centerY - ((arcRadius - 50) * sin(Math.toRadians(time + ANGLE_OFF_SET).toFloat()))
    }

    private fun timePosition() {
        paintTime.getTextBounds("$hours:$minutes", 0 , "$hours.$minutes".length, conTime)
        timeX = centerX - (conTime.width() / 2)
        timeY = centerY - ((paintTime.descent() - paintTime.ascent() / 2) - sizeTime)
    }

    private fun timeToAngle(time: Double) = time * 3 * 2

    private fun create() = mutableListOf<Line>().apply {
        repeat(SHAPE_TIME) { add(Line()) }
    }

    private fun initPaint() {
        paintShapeTime = paintShapeTimeFun()
        paintShapeCircle = paintShapeCircleFun()
        paintTime = paintTimeFun()
        paintAmPmTime = paintTextTimeFun()
    }

    private fun paintShapeCircleFun() = Paint(Paint.ANTI_ALIAS_FLAG). apply {
        color = colorShapeCircle
    }

    private fun paintTimeFun() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorTime
        style = Paint.Style.FILL
        textSize = sizeTime
    }

    private fun paintTextTimeFun() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorAmPm
        style = Paint.Style.FILL
        textSize = sizeAmPmTime
    }

    private fun paintShapeTimeFun() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorShapeTime
        strokeWidth = 5f
    }

    private fun TypedArray.loadAttr() {
        colorShapeTime = getColor(R.styleable.Clock_color_shape_time, colorShapeTime)
        colorShapeCircle = getColor(R.styleable.Clock_color_shape_circle, colorShapeCircle)
        colorTime = getColor(R.styleable.Clock_color_time, colorTime)
        colorAmPm = getColor(R.styleable.Clock_color_am_pm, colorAmPm)
        is24Hours = getBoolean(R.styleable.Clock_is24hours, is24Hours)
    }

    init {
        attr.let {
            context.theme.obtainStyledAttributes(
                it,
                R.styleable.Clock,
                0, 0).apply {
                try{ loadAttr() } finally {
                    recycle()
                }
            }
        }
        initPaint()
    }

    companion object {
        private const val ANGLE_OFF_SET: Double = 90.0
        private const val SHAPE_TIME = 120
        private const val RAD_END_SHAPE = 30
        private const val DEFAULT_SIZE = 600
    }


}

data class Line(
    var left: Float = 0f,
    var top: Float = 0f,
    var right: Float = 0f,
    var bottom: Float = 0f ) {
    fun set (left: Float, top: Float, right: Float, bottom: Float) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }
}
