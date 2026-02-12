package ji.shop

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ji.shop.exts.collect
import ji.shop.utils.FragmentUtils

class ShopActivity : AppCompatActivity(R.layout.activity_shop) {
    private val viewModel by viewModels<ShopViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserves()
    }

    private fun initObserves() {
        collect(channel = viewModel.gotoFragmentEvent) {
            doOnGoto(it)
        }

        collect(channel = viewModel.backEvent) {
            doOnBack()
        }
    }

    private fun doOnGoto(action: () -> Fragment) {
        FragmentUtils.replace(
            FragmentUtils.ReplaceOption.with(this)
                .setContainerId(R.id.fl_replace)
                .setFragment(action())
                .addToBackStack(true)
        )
    }

    private fun doOnBack() {
        if (FragmentUtils.isFirstFragment(this)) {
            finish()
            return
        }
        FragmentUtils.popBack(this)
    }
}