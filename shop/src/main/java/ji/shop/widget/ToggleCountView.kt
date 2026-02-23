package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import ji.shop.R
import ji.shop.databinding.ToggleCountViewBinding

class ToggleCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, def: Int = 0
) : LinearLayout(context, attrs, def) {
    private val binding = ToggleCountViewBinding.inflate(LayoutInflater.from(context), this)
    var currentCount = 0
    private var listener: OnCountListener? = null
    private var hideWhenCountingZero = false
    private var useExpandView = false

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ToggleCountView, def, 0)
            hideWhenCountingZero =
                typedArray.getBoolean(R.styleable.ToggleCountView_tg_hide_when_counting_zero, false)
            useExpandView =
                typedArray.getBoolean(R.styleable.ToggleCountView_tg_use_expanded_view, false)
        }
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

    fun setHideWhenCountingZero(hide: Boolean) {
        hideWhenCountingZero = hide
        updateCount()
    }

    fun setCount(count: Int) {
        currentCount = count
        updateCount()
    }

    private fun counting(i: Int) {
        currentCount += i
        if (currentCount < 0) currentCount = 0
        updateCount()
        listener?.onCount(currentCount)
    }

    private fun updateCount() {
        binding.tvCount.text = currentCount.toString()
        val hasCount = currentCount > 0 || useExpandView || (binding.btnMinus.isVisible && !hideWhenCountingZero)
        isVisible = hasCount || !hideWhenCountingZero
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