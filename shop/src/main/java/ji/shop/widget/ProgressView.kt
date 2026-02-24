package ji.shop.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ji.shop.R
import ji.shop.databinding.ProgressViewBinding

class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, def: Int = 0
) : LinearLayout(context, attrs, def) {

    private val binding = ProgressViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.ProgressItemView, def, 0)

            val progress = typedArray.getInt(
                R.styleable.ProgressItemView_progressValue, 0
            )

            val indicatorColor = typedArray.getColor(
                R.styleable.ProgressItemView_indicatorColor, Color.BLUE
            )

            val trackColor = typedArray.getColor(
                R.styleable.ProgressItemView_trackColor, Color.GRAY
            )
            binding.progressBar.apply {
                setProgress(progress)
                setIndicatorColor(indicatorColor)
                setTrackColor(trackColor)
            }
        }
        initViews()
    }

    private fun initViews() {

    }

    fun setProgress(percent: Int) {
        binding.progressBar.setProgressCompat(percent, true)
        binding.tvPercent.text = "$percent %"
    }

    fun setIndicatorColor(color: Int) {
        binding.progressBar.setIndicatorColor(color)
    }

    fun setTrackColor(color: Int) {
        binding.progressBar.trackColor = color
    }
}