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
            updateCount()
            btnPlus.setOnClickListener { counting(1) }
            btnMinus.setOnClickListener { counting(-1) }
            btnAdd.setOnClickListener { counting(1) }
        }
    }

    fun setCount(count: Int) {
        currentCount = count
        updateCount()
    }

    private fun counting(i: Int) {
        currentCount += i
        updateCount()
        listener?.onCount(currentCount)
    }

    private fun updateCount() {
        binding.tvCount.text = currentCount.toString()
        val hasCount = currentCount > 0
        if (!hasCount || binding.btnMinus.isVisible) return
        binding.btnMinus.isVisible = hasCount
        binding.tvCount.isVisible = hasCount
        binding.btnPlus.isVisible = hasCount
        binding.btnAdd.isVisible = !hasCount
    }

    fun interface OnCountListener {
        fun onCount(count: Int)
    }
}