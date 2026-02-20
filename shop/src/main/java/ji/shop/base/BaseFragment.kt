package ji.shop.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ji.shop.R
import ji.shop.ShopViewModel
import ji.shop.data.ResultWrapper
import ji.shop.exts.collect
import ji.shop.exts.onBackPressedOverride
import ji.shop.widget.StateWrapperView
import kotlinx.coroutines.flow.Flow

abstract class BaseFragment(layoutRes: Int) : Fragment(layoutRes),
    StateWrapperView.StateWrapperViewListener {
    open val shopViewModel by activityViewModels<ShopViewModel>()
    open val stateWrapperView by lazy { view?.findViewById<StateWrapperView>(R.id.state_view) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateWrapperView?.setListener(this)
        onBackPressedOverride {
            doOnBack()
        }
    }

    open fun doOnBack() {
        shopViewModel.back()
    }

    fun goto(fragment: Fragment) {
        shopViewModel.goto { fragment }
    }

    fun <T> collectWithProgress(
        stateWrapperView: StateWrapperView? = this.stateWrapperView,
        flow: Flow<ResultWrapper<T>>,
        action: (T) -> Unit
    ) {
        collect(flow = flow) { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    stateWrapperView?.updateState(result) ?: showProgress(false)
                    action(result.data)
                }

                is ResultWrapper.Loading -> {
                    stateWrapperView?.updateState(result) ?: showProgress(true)
                }

                is ResultWrapper.Failure -> {
                    stateWrapperView?.updateState(result) ?: run {
                        showProgress(false)
                        showError(result.error)
                    }
                }

                is ResultWrapper.Empty -> {
                    stateWrapperView?.updateState(result) ?: showProgress(false)
                }

                is ResultWrapper.None -> {
                }
            }
        }
    }

    protected fun showProgress(show: Boolean) {}
    protected fun showError(error: Throwable) {}

    override fun onRetry() {

    }
}