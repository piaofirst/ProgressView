package com.piaofirst.progressviewlibrary

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by gjc on 2017/7/21.
 */
class ProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    var radius = 10f
        set(value) {
            field = value
            invalidate()
        }

    var borderWidth = 1f
        set(value) {
            field = value
            borderPaint.strokeWidth = value
            invalidate()
        }

    var color = Color.GREEN
        set(value) {
            field = value
            borderPaint.color = value
            bgPaint.color = value
            frontPaint.color = value
            textPaint.color = value
            invalidate()
        }

    var progress = 0f
        set(value) {
            field = value
            invalidate()
        }
    var state = ProgressState.Idle
        set(value) {
            field = value
            invalidate()
        }

    var textSize = defaultFontSize
        set(value) {
            field = value
            textPaint.textSize = value
            invalidate()
        }

    enum class ProgressState {
        Loading,
        Paused,
        Finished,
        Idle
    }

    var onStart: (() -> Unit)? = null
    var onPause: (() -> Unit)? = null
    var onResume: (() -> Unit)? = null
    var onOpen: (() -> Unit)? = null

    var idleText = "下载"
    var loadingTextRender = { progress: Float -> "${(progress * 100).toInt()}%" }
    var pauseText = "暂停"
    var finishedText = "完成"

    var rectF = RectF(0f, 0f, 0f, 0f)
    var maskRectF = RectF(0f, 0f, 0f, 0f)
    var textRect = Rect()

    var borderPaint = Paint()
    var bgPaint = Paint()
    var frontPaint = Paint()
    var textPaint = Paint()

    private var viewWidth = 0
    private var viewHeight = 0

    companion object {
        private val defaultFontSize = 30f
    }

    init {
        bgPaint.color = color
        bgPaint.isAntiAlias = true

        frontPaint.color = color
        frontPaint.isAntiAlias = true
        frontPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        borderPaint.color = color
        borderPaint.isAntiAlias = true
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth

        textPaint.color = color
        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        textPaint.textAlign = Paint.Align.LEFT

        setOnClickListener {
            when (state) {
                ProgressState.Idle -> {
                    state = ProgressState.Loading
                    onStart?.invoke()
                }
                ProgressState.Loading -> {
                    state = ProgressState.Paused
                    onPause?.invoke()
                }
                ProgressState.Paused -> {
                    state = ProgressState.Loading
                    onResume?.invoke()
                }
                ProgressState.Finished -> {
                    onOpen?.invoke()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var layoutId = canvas.saveLayer(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvas.drawRoundRect(rectF, radius, radius, bgPaint)

        maskRectF.left = rectF.left + progress * (rectF.right - rectF.left)
        canvas.drawRect(maskRectF, frontPaint)
        canvas.drawRoundRect(rectF, radius, radius, borderPaint)

        val text = pauseText()
        textPaint.getTextBounds(text, 0, text.length, textRect)
        val textX = viewWidth / 2 - textRect.centerX()
        val textY = viewHeight / 2 - textRect.centerY()
        canvas.drawText(text, textX.toFloat(), textY.toFloat(), textPaint)

        canvas.restoreToCount(layoutId)
    }

    private fun pauseText() = when (state) {
        ProgressState.Idle -> idleText
        ProgressState.Paused -> pauseText
        ProgressState.Finished -> finishedText
        else -> loadingTextRender(progress)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = measuredWidth
        viewHeight = measuredHeight

        rectF.left = borderWidth / 2
        rectF.top = borderWidth / 2
        rectF.right = viewWidth - borderWidth / 2
        rectF.bottom = viewHeight - borderWidth / 2

        maskRectF.left = 0f
        maskRectF.top = 0f
        maskRectF.right = viewWidth.toFloat()
        maskRectF.bottom = viewHeight.toFloat()
    }

}
