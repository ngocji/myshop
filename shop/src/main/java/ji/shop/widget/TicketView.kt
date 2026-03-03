package ji.shop.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import ji.shop.R

class TicketView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val radius = dp(16f)
    private val cutRadius = dp(12f)

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp(1f)
        color = Color.parseColor("#22FFFFFF")
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    init {
        bgPaint.shader = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            Color.parseColor("#2B2E33"),
            Color.parseColor("#1E2227"),
            Shader.TileMode.CLAMP
        )

        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = dp(1f)
        strokePaint.color = Color.TRANSPARENT
    }

    override fun dispatchDraw(canvas: Canvas) {

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        val dividerY = getDriverY()   // position dashed line

        // background
        canvas.drawRoundRect(rect, radius, radius, bgPaint)

        // dashed divider
        canvas.drawLine(0f, dividerY, width.toFloat(), dividerY, dashPaint)

        // cut circle left
        val clearPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        canvas.drawCircle(0f, dividerY, cutRadius, clearPaint)

        // cut circle right
        canvas.drawCircle(width.toFloat(), dividerY, cutRadius, clearPaint)

        // stroke
        canvas.drawRoundRect(rect, radius, radius, strokePaint)

        super.dispatchDraw(canvas)
    }

    private fun getDriverY(): Float {
        val header = findViewById<View?>(R.id.header)
        return header?.measuredHeight?.takeIf { it > 0 }?.toFloat() ?: (height * 0.32f)
    }

    private fun dp(v: Float) = v * resources.displayMetrics.density
}