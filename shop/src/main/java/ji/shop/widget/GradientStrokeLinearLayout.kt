package ji.shop.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.LinearLayout
import ji.shop.R
import androidx.core.graphics.toColorInt

open class GradientStrokeLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val radius by lazy { context.resources.getDimension(R.dimen.large_radius) }
    private val strokeWidth by lazy { context.resources.getDimensionPixelOffset(R.dimen._1dp) }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        alpha = 26
    }

    private val rect = RectF()

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (rect.isEmpty) {
            rect.set(
                strokeWidth / 2f,
                strokeWidth / 2f,
                width - strokeWidth / 2f,
                height - strokeWidth / 2f
            )
        }

        // background
        canvas.drawRoundRect(rect, radius, radius, bgPaint)

        // gradient stroke
        if (strokePaint.shader == null) {
            initStrokeShader()
        }

        canvas.drawRoundRect(rect, radius, radius, strokePaint)
    }

    private fun initStrokeShader() {
        strokePaint.setShader(
            LinearGradient(
                0f, 0f,
                0f, height.toFloat(),
                intArrayOf(
                    "#66FFFFFF".toColorInt(),
                    "#0DFFFFFF".toColorInt(),
                    "#1AFFFFFF".toColorInt()
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        )
    }
}