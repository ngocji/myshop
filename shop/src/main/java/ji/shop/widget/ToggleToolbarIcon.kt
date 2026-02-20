package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import ji.shop.R

class ToggleToolbarIcon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {
    init {
        val padding = context.resources.getDimensionPixelSize(R.dimen.small_padding)
        setPadding(padding, padding, padding, padding)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (isSelected) {
            setBackgroundResource(R.drawable.bg_highlight_2)
            setColorFilter(context.getColor(R.color.colorAccent))
        } else {
            setBackgroundResource(R.drawable.bg_secondary_rounded_with_stroke)
            setColorFilter(context.getColor(R.color.colorInvertAccent))
        }
    }
}