package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import ji.shop.R
import ji.shop.databinding.LayoutCustomEdittextBinding

class CustomEdittext @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: LayoutCustomEdittextBinding

    init {
        orientation = VERTICAL
        binding = LayoutCustomEdittextBinding.inflate(LayoutInflater.from(context), this)

        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.CustomEdittext, defStyleAttr, 0)

            with(binding) {
                val title = typedArray.getString(R.styleable.CustomEdittext_cs_title)
                tvTitle.text = title.orEmpty()
                tvTitle.isVisible = !title.isNullOrEmpty()

                edt.hint = typedArray.getString(R.styleable.CustomEdittext_cs_hint)
                setText(typedArray.getString(R.styleable.CustomEdittext_cs_text))

                val icon = typedArray.getResourceId(R.styleable.CustomEdittext_cs_image_start, 0)
                setStartIcon(icon)

            }
        }
    }

    fun setText(text: String?) {
        binding.edt.setText(text.orEmpty())
    }

    fun getText(): String {
        return binding.edt.text.toString()
    }

    fun setStartIcon(icon: Int) {
        with(binding) {
            if (icon <= 0) {
                imageIconStart.isVisible = false
                edt.updatePaddingRelative(start = edt.paddingEnd)
                return
            }

            imageIconStart.isVisible = true
            imageIconStart.setImageResource(icon)
            edt.updatePaddingRelative(
                start = (edt.paddingEnd * 2) + context.resources.getDimensionPixelOffset(
                    R.dimen._34dp
                )
            )
        }
    }
}