package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import ji.shop.R
import ji.shop.databinding.DropDownViewBinding

class DropDownView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: DropDownViewBinding

    init {
        binding = DropDownViewBinding.inflate(LayoutInflater.from(context), this)
        setBackgroundResource(R.drawable.bg_secondary_rounded_with_stroke)
        gravity = Gravity.CENTER
    }

    fun <T> setData(items: List<T>?, selectItem: T?, onSelected: (T) -> Unit) {
        val spinnerAdapter = ArrayAdapter(
            binding.spinner.context,
            android.R.layout.simple_spinner_item,
            items ?: emptyList()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.run {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    items?.getOrNull(position)?.let { onSelected(it) }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            setSelect(selectItem)
        }

        doOnPreDraw { container ->
            binding.spinner.dropDownVerticalOffset = container.measuredHeight - 20
            binding.spinner.dropDownHorizontalOffset =
                -context.resources.getDimensionPixelOffset(R.dimen.large_padding)
            binding.spinner.dropDownWidth = container.measuredWidth
        }
    }

    fun <T> setSelect(item: T?) {
        val index =
            (binding.spinner.adapter as? ArrayAdapter<T>)?.getPosition(item)?.takeIf { it != -1 }
                ?: return
        binding.spinner.setSelection(index)
    }
}