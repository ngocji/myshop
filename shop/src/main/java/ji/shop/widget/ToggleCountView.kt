package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import ji.shop.databinding.ToggleCountViewBinding

class ToggleCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = ToggleCountViewBinding.inflate(LayoutInflater.from(context), this)
    private var currentCount = 0
    private var listener: OnCountListener? = null

    init {
        initViews()
    }

    fun setListener(listener: OnCountListener) {
        this.listener = listener
    }

    private fun initViews() {
        with(binding) {
            updateCount(true)
            btnPlus.setOnClickListener { counting(1) }
            btnMinus.setOnClickListener { counting(-1) }
            btnAdd.setOnClickListener { counting(1) }
        }
    }

    fun setCount(count: Int, skipZero: Boolean = true) {
        currentCount = count
        updateCount(skipZero)
    }

    private fun counting(i: Int) {
        currentCount += i
        if (currentCount < 0) currentCount = 0
        updateCount(true)
        listener?.onCount(currentCount)
    }

    private fun updateCount(skipZero: Boolean) {
        binding.tvCount.text = currentCount.toString()
        val hasCount = currentCount > 0 || (binding.btnMinus.isVisible && skipZero)
        with(binding) {
            btnMinus.isVisible = hasCount
            tvCount.isVisible = hasCount
            btnPlus.isVisible = hasCount
            btnAdd.isVisible = !hasCount
        }
    }

    fun interface OnCountListener {
        fun onCount(count: Int)
    }
}