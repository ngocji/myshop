package ji.shop.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import ji.shop.R

class TicketView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val radius = dp(16f)
    private val cutRadius = dp(12f)

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorTicket)
        style = Paint.Style.FILL
    }

    private val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp(1.5f)
        color = Color.parseColor("#22FFFFFF")
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private val ticketPath = Path()
    private val cutPath = Path()

    override fun dispatchDraw(canvas: Canvas) {

        val dividerY = getDividerY()

        buildTicketPath(dividerY)

        // draw ticket background
        canvas.drawPath(ticketPath, bgPaint)

        // dashed divider
        canvas.drawLine(
            cutRadius,
            dividerY,
            width - cutRadius,
            dividerY,
            dashPaint
        )

        super.dispatchDraw(canvas)
    }

    private fun buildTicketPath(dividerY: Float) {

        ticketPath.reset()
        cutPath.reset()

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        // ticket base
        ticketPath.addRoundRect(rect, radius, radius, Path.Direction.CW)

        // cut circles
        cutPath.addCircle(0f, dividerY, cutRadius, Path.Direction.CW)
        cutPath.addCircle(width.toFloat(), dividerY, cutRadius, Path.Direction.CW)

        // subtract circles
        ticketPath.op(cutPath, Path.Op.DIFFERENCE)
    }

    private fun getDividerY(): Float {
        val header = findViewById<View?>(R.id.header)
        return header?.bottom?.toFloat() ?: (height * 0.32f)
    }

    private fun dp(v: Float) = v * resources.displayMetrics.density
}