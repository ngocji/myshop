package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import ji.shop.R
import ji.shop.databinding.DropDownViewBinding

class DropDownView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = DropDownViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        setBackgroundResource(R.drawable.bg_secondary_rounded_with_stroke)
        gravity = Gravity.CENTER
    }

    fun <T> setData(items: List<T>) {
        val adapter = ArrayAdapter(
            binding.spinner.context,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
    }
}