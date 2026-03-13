package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import ji.shop.R
import ji.shop.data.domain.Status

class StatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextView(context, attrs) {
    init {
        TextViewCompat.setTextAppearance(this, R.style.fontMedium)
        val vPadding = resources.getDimensionPixelSize(R.dimen._5dp)
        val hPadding = resources.getDimensionPixelSize(R.dimen._10dp)
        setPadding(hPadding, vPadding, hPadding, vPadding)
        minWidth = resources.getDimensionPixelSize(R.dimen._90dp)
    }

    fun setState(status: Status) {
        val colorRes: Int
        val textRes: Int
        val bgRes: Int
        when (status) {
            Status.COMPLETE -> {
                colorRes = R.color.colorStateComplete
                textRes = R.string.text_state_complete
                bgRes = R.drawable.bg_state_complete
            }

            Status.PAID -> {
                colorRes = R.color.colorStatePaid
                textRes = R.string.text_state_paid
                bgRes = R.drawable.bg_state_paid
            }

            Status.REFUND -> {
                colorRes = R.color.colorStateRefund
                textRes = R.string.text_state_refund
                bgRes = R.drawable.bg_state_refund
            }

            else -> {
                colorRes = R.color.colorStateInProgress
                textRes = R.string.text_state_inpgoress
                bgRes = R.drawable.bg_state_inprogress
            }
        }
        setText(textRes)
        setTextColor(ContextCompat.getColor(context, colorRes))
        setBackgroundResource(bgRes)
        gravity = Gravity.CENTER
    }
}