package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import ji.shop.R
import ji.shop.data.domain.ResultWrapper
import ji.shop.databinding.LayoutStateEmptyBinding
import ji.shop.databinding.LayoutStateErrorBinding
import ji.shop.databinding.LayoutStateLoadingBinding

class StateWrapperView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private var listener: StateWrapperViewListener? = null

    init {
        addView(LayoutStateLoadingBinding.inflate(LayoutInflater.from(context)).root)
        addView(LayoutStateEmptyBinding.inflate(LayoutInflater.from(context)).root)
        addView(LayoutStateErrorBinding.inflate(LayoutInflater.from(context)).root)

        findViewById<View>(R.id.btn_retry)?.setOnClickListener {
            updateState(ResultWrapper.Loading)
            listener?.onRetry()
        }
    }

    fun setListener(listener: StateWrapperViewListener) {
        this.listener = listener
    }

    fun updateState(state: ResultWrapper<*>) {
        when (state) {
            is ResultWrapper.Loading -> {
                showView(R.id.progress_view)
            }

            is ResultWrapper.Empty -> {
                showView(R.id.empty_view)
            }

            is ResultWrapper.Failure -> {
                showView(R.id.error_view)
                findViewById<TextView>(R.id.tv_error)?.text = state.error.message
            }

            else -> {
                showView()
            }
        }
    }

    private fun showView(vararg viewIds: Int = intArrayOf()) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isVisible = viewIds.contains(child.id)
        }
    }

    interface StateWrapperViewListener {
        fun onRetry()
    }
}