package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ji.shop.databinding.MyBalanceViewBinding
import ji.shop.utils.NumberFormater

class MyBalanceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val binding = MyBalanceViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding
    }

    fun setPrice(price: Double?) {
        binding.tvPrice.text = NumberFormater.formatNumberLocale(price ?: 0.0)
    }
}