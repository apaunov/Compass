package com.andyapps.compass.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import com.andyapps.compass.R
import com.andyapps.compass.utilities.Utils


open class CompassView : View {
    private lateinit var markerPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var circlePaint: Paint
    private lateinit var northString: String
    private lateinit var eastString: String
    private lateinit var southString: String
    private lateinit var westString: String

    private var textHeight = 0
    var bearing = 0.0f
        set(value) {
            field = value
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
        }

    constructor(context: Context) : super(context) {
        initCompassView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initCompassView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initCompassView()
    }

    // ANDROID OVERRIDEN METHODS

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val measuredWidth = measure(widthMeasureSpec)
        val measuredHeight = measure(heightMeasureSpec)

        val d = Math.min(measuredWidth, measuredHeight)

        setMeasuredDimension(d, d)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cx = measuredWidth / 2
        val cy = measuredHeight / 2
        val radius = Math.min(cx, cy)

        canvas?.drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), circlePaint)
        canvas?.save()
        canvas?.rotate(-bearing, cx.toFloat(), cy.toFloat())

        val textWidth = textPaint.measureText("W")
        val cardinalX = cx - textWidth / 2
        val cardinalY = cy - radius + textHeight

        for (i in 0 until 24) {
            canvas?.drawLine(cx.toFloat(), (cy - radius).toFloat(), cx.toFloat(), (cy - radius + 10).toFloat(), markerPaint)
            canvas?.save()
            canvas?.translate(0f, textHeight.toFloat())

            if (i % 6 == 0) {
                var dirString = ""

                when (i) {
                    0 -> {
                        dirString = northString

                        val arrowY = 2 * textHeight

                        canvas?.drawLine(cx.toFloat(), arrowY.toFloat(), (cx - 5).toFloat(), (3 * textHeight).toFloat(), markerPaint)
                        canvas?.drawLine(cx.toFloat(), arrowY.toFloat(), (cx + 5).toFloat(), (3 * textHeight).toFloat(), markerPaint)
                    }

                    6 -> {
                        dirString = eastString
                    }

                    12 -> {
                        dirString = southString
                    }

                    18 -> {
                        dirString = westString
                    }
                }

                canvas?.drawText(dirString, cardinalX, cardinalY.toFloat(), textPaint)
            } else if (i % 3 == 0) {
                val angle = (i * 15).toString()
                val angleTextWidth = textPaint.measureText(angle)
                val angleTextX = cx - angleTextWidth / 2
                val angleTextY = cy - radius + textHeight

                canvas?.drawText(angle, angleTextX, angleTextY.toFloat(), textPaint)
            }

            canvas?.restore()
            canvas?.rotate(15f, cx.toFloat(), cy.toFloat())
        }

        canvas?.restore()
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        super.dispatchPopulateAccessibilityEvent(event)

        return if (isShown) {
            event?.text?.add(bearing.toString())
            true
        } else {
            false
        }
    }

    // HELPER METHODS

    private fun initCompassView() {
        isFocusable = true

        val r = this.resources

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint.color = r.getColor(R.color.background_color)
        circlePaint.strokeWidth = 1f
        circlePaint.style = Paint.Style.FILL_AND_STROKE

        northString = r.getString(R.string.cardinal_north)
        eastString = r.getString(R.string.cardinal_east)
        southString = r.getString(R.string.cardinal_south)
        westString = r.getString(R.string.cardinal_west)

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = r.getColor(R.color.text_color)
        textPaint.textSize = Utils.spToPx(context, 16f)

        textHeight = textPaint.measureText("yY").toInt()

        markerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        markerPaint.color = r.getColor(R.color.marker_color)
    }

    private fun measure(measureSpec: Int): Int {
        val result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        result = if (specMode == MeasureSpec.UNSPECIFIED) {
            200
        } else {
            specSize
        }

        return result
    }
}