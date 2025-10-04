package com.game.twenty48

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var gameManager: GameManager? = null
    private val gridSize = 4
    private var cellSize = 0f
    private var gridMargin = 16f
    private var cellMargin = 8f

    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#BBADA0")
        isAntiAlias = true
    }

    private val cellPaint = Paint().apply {
        color = Color.parseColor("#CDC1B4")
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = (e2.x - (e1?.x ?: 0f))
            val diffY = (e2.y - (e1?.y ?: 0f))

            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > 100 && abs(velocityX) > 100) {
                    if (diffX > 0) {
                        gameManager?.move(Direction.RIGHT)
                    } else {
                        gameManager?.move(Direction.LEFT)
                    }
                    return true
                }
            } else {
                if (abs(diffY) > 100 && abs(velocityY) > 100) {
                    if (diffY > 0) {
                        gameManager?.move(Direction.DOWN)
                    } else {
                        gameManager?.move(Direction.UP)
                    }
                    return true
                }
            }
            return false
        }
    })

    fun setGameManager(manager: GameManager) {
        gameManager = manager
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val size = min(w, h).toFloat()
        cellSize = (size - gridMargin * 2 - cellMargin * (gridSize + 1)) / gridSize
        gridMargin = (w - (cellSize * gridSize + cellMargin * (gridSize + 1))) / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background
        canvas.drawRoundRect(
            0f, 0f, width.toFloat(), width.toFloat(),
            8f, 8f, backgroundPaint
        )

        // Draw empty cells
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                drawCell(canvas, row, col, null)
            }
        }

        // Draw tiles
        gameManager?.getTiles()?.forEach { tile ->
            drawCell(canvas, tile.row, tile.col, tile)
        }
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int, tile: Tile?) {
        val left = gridMargin + col * (cellSize + cellMargin) + cellMargin
        val top = gridMargin + row * (cellSize + cellMargin) + cellMargin
        val right = left + cellSize
        val bottom = top + cellSize

        val rect = RectF(left, top, right, bottom)

        if (tile == null) {
            canvas.drawRoundRect(rect, 4f, 4f, cellPaint)
        } else {
            val tilePaint = Paint(cellPaint).apply {
                color = getTileColor(tile.value)
            }
            canvas.drawRoundRect(rect, 4f, 4f, tilePaint)

            textPaint.color = if (tile.value <= 4) Color.parseColor("#776E65") else Color.WHITE
            textPaint.textSize = when {
                tile.value < 100 -> cellSize * 0.5f
                tile.value < 1000 -> cellSize * 0.4f
                else -> cellSize * 0.35f
            }

            val centerX = left + cellSize / 2
            val centerY = top + cellSize / 2 - (textPaint.descent() + textPaint.ascent()) / 2

            canvas.drawText(tile.value.toString(), centerX, centerY, textPaint)
        }
    }

    private fun getTileColor(value: Int): Int {
        return when (value) {
            2 -> Color.parseColor("#EEE4DA")
            4 -> Color.parseColor("#EDE0C8")
            8 -> Color.parseColor("#F2B179")
            16 -> Color.parseColor("#F59563")
            32 -> Color.parseColor("#F67C5F")
            64 -> Color.parseColor("#F65E3B")
            128 -> Color.parseColor("#EDCF72")
            256 -> Color.parseColor("#EDCC61")
            512 -> Color.parseColor("#EDC850")
            1024 -> Color.parseColor("#EDC53F")
            2048 -> Color.parseColor("#EDC22E")
            else -> Color.parseColor("#3C3A32")
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }
}

