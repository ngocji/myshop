package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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

    enum class State {
        NONE, LOADING, EMPTY, ERROR, SUCCESS
    }

    init {
        addView(LayoutStateLoadingBinding.inflate(LayoutInflater.from(context)).root)
        addView(LayoutStateEmptyBinding.inflate(LayoutInflater.from(context)).root)
        addView(LayoutStateErrorBinding.inflate(LayoutInflater.from(context)).root)

        findViewById<View>(R.id.btn_retry)?.setOnClickListener {
            updateStateWithResult(ResultWrapper.Loading)
            listener?.onRetry()
        }

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.StateWrapperView)
            val progressBackground = typedArray.getColor(
                R.styleable.StateWrapperView_st_progress_background,
                ContextCompat.getColor(context, R.color.colorBackground)
            )
            findViewById<View>(R.id.progress_view)?.setBackgroundColor(progressBackground)

            typedArray.getString(R.styleable.StateWrapperView_st_empty_text)
                .takeIf { emptyText -> !emptyText.isNullOrBlank() }
                .let { emptyText -> findViewById<TextView>(R.id.tv_empty)?.text = emptyText }

            typedArray.recycle()
        }
    }

    fun setListener(listener: StateWrapperViewListener) {
        this.listener = listener
    }

    fun updateStateWithResult(state: ResultWrapper<*>) {
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

    fun updateState(state: State) {
        when (state) {
            State.LOADING -> {
                showView(R.id.progress_view)
            }

            State.EMPTY -> {
                showView(R.id.empty_view)
            }

            State.ERROR -> {
                showView(R.id.error_view)
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