package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import ji.shop.R
import ji.shop.data.State

class StateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextView(context, attrs) {
    init {
        TextViewCompat.setTextAppearance(this, R.style.fontMedium)
        val vPadding = resources.getDimensionPixelSize(R.dimen._5dp)
        val hPadding = resources.getDimensionPixelSize(R.dimen._10dp)
        setPadding(hPadding, vPadding, hPadding, vPadding)
        minWidth = resources.getDimensionPixelSize(R.dimen._90dp)
    }

    fun setState(state: State) {
        val colorRes: Int
        val textRes: Int
        val bgRes: Int
        when (state) {
            State.COMPLETE -> {
                colorRes = R.color.colorStateComplete
                textRes = R.string.text_state_complete
                bgRes = R.drawable.bg_state_complete
            }

            State.PAID -> {
                colorRes = R.color.colorStatePaid
                textRes = R.string.text_state_paid
                bgRes = R.drawable.bg_state_paid
            }

            State.REFUND -> {
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
        setTextColor(context.getColor(colorRes))
        setBackgroundResource(bgRes)
    }
}