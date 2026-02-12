package ji.shop.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ji.shop.ShopViewModel
import ji.shop.exts.onBackPressedOverride

abstract class BaseFragment(layoutRes: Int) : Fragment(layoutRes) {
    open val shopViewModel by activityViewModels<ShopViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
}