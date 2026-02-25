package ji.shop

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ji.shop.data.TabType
import ji.shop.databinding.ActivityShopBinding
import ji.shop.dialog.TurnOnNfcDialog
import ji.shop.dialog.ViewCartDialog
import ji.shop.exts.collect
import ji.shop.utils.FragmentUtils

class ShopActivity : AppCompatActivity() {
    private val viewModel by viewModels<ShopViewModel>()
    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initObserves()
    }

    private fun initViews() {
        with(binding) {
            tabViews.setData(
                items = TabType.entries.toList(),
                selectedIndex = viewModel.tabTabTypeState.value.ordinal,
                onGetTitle = { tab -> getString(tab.titleRes) }
            ) { selected ->
                viewModel.changeTabType(selected)
            }

            btnNfc.setOnClickListener { doToggleNFC() }
            btnViewCart?.setOnClickListener { doViewCart() }
            btnStream.setOnClickListener { }
        }
    }

    private fun initObserves() {
        collect(channel = viewModel.gotoFragmentEvent) {
            doOnGoto(it)
        }

        collect(channel = viewModel.backEvent) {
            doOnBack()
        }

        collect(flow = viewModel.isNfcEnabledState) { enable ->
            binding.btnNfc.isSelected = enable
        }

        collect(flow = viewModel.myBalanceState) { price ->
            binding.myBalance?.setPrice(price)
        }

        collect(flow = viewModel.tabTabTypeState) { tab ->
            binding.tabViews.setSelected(tab)
        }

        collect(flow = viewModel.shopCategoriesFlow) { data ->
            binding.shopCategoryDropDown.setData(data?.first, data?.second)
        }

        collect(channel = viewModel.viewCartEvent) {
            doViewCart()
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

    private fun doViewCart() {
        ViewCartDialog.newInstance(viewModel.getCartItems()) { carts, isGotoCheckout ->
            viewModel.updateCarts(carts)
        }
            .show(supportFragmentManager)
    }

    private fun doToggleNFC() {
        if (viewModel.isNfcEnabled()) {
            viewModel.setNfcEnabled(false)
        } else {
            TurnOnNfcDialog.newInstance {
                viewModel.setNfcEnabled(true)
            }
                .show(supportFragmentManager)
        }
    }
}