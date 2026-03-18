package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ji.shop.R
import ji.shop.databinding.ItemTitleBinding

class TitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: ItemTitleBinding

    init {
        orientation = VERTICAL
        binding = ItemTitleBinding.inflate(LayoutInflater.from(context), this, true)

        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.TitleView, defStyleAttr, 0)

            with(binding) {
                tvTitle.text = typedArray.getString(R.styleable.TitleView_tv_title)
                tvValue.text = typedArray.getString(R.styleable.TitleView_tv_value)
            }
        }
    }

    fun setTitle(text: String?) {
        binding.tvTitle.text = text.orEmpty()
    }

    fun setValue(value: String?) {
        binding.tvValue.text = value.orEmpty()
    }
}