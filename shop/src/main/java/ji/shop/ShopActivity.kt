package ji.shop

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ji.shop.data.domain.CardMethod
import ji.shop.data.domain.CreditInfo
import ji.shop.data.domain.CustomerInfo
import ji.shop.data.domain.ResultWrapper
import ji.shop.data.domain.TabType
import ji.shop.data.dto.RefreshTokenAuth
import ji.shop.databinding.ActivityShopBinding
import ji.shop.dialog.CheckoutDialog
import ji.shop.dialog.EditManualCardDialog
import ji.shop.dialog.TurnOnNfcDialog
import ji.shop.dialog.ViewCartDialog
import ji.shop.exts.changeEnabled
import ji.shop.exts.collect
import ji.shop.exts.collectOne
import ji.shop.utils.FragmentUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopActivity : AppCompatActivity() {
    private val viewModel by viewModels<ShopViewModel>()
    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initObserves()
        RefreshTokenAuth.onAuthFailedAction = {
            // todo navigate to login
            lifecycleScope.launch(Dispatchers.Main) {
                binding.flReplace.isVisible = false
            }
        }
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
            binding.shopCategoryDropDown.setData(data, viewModel.shopCategoryState.value) { item ->
                viewModel.setViewShopCategory(item)
            }
        }

        collect(flow = viewModel.shopCategoryState) { shop ->
            binding.shopCategoryDropDown.setSelect(shop)
        }

        collect(channel = viewModel.gotoFragmentEvent) {
            doOnGoto(it)
        }

        collect(channel = viewModel.backEvent) {
            doOnBack()
        }
        collect(channel = viewModel.viewCartEvent) {
            doViewCart()
        }
        collect(channel = viewModel.loadingGlobalEvent) {
            showProgress(it)
        }
        collect(flow = viewModel.cartsState) {
            binding.btnViewCart?.changeEnabled(it.data.isNotEmpty())
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
        ViewCartDialog.newInstance { updatedCarts ->
            CheckoutDialog.newInstance(
                updatedCarts,
                viewModel.getUsedCardMethod(),
                object : CheckoutDialog.Listener {
                    override fun onUpdateCustomerInfo(customerInfo: CustomerInfo?) {
                        viewModel.updateCustomerInfo(customerInfo)
                    }

                    override fun onDone(method: CardMethod) {
                        if (method == CardMethod.Credit) {
                            EditManualCardDialog
                                .newInstance(viewModel.creditCardInfo.value) { newCreditCard ->
                                    doCreateCheckout(method, newCreditCard)
                                }
                                .show(supportFragmentManager)
                        } else {
                            doCreateCheckout(method, null)
                        }
                    }
                })
                .show(supportFragmentManager)
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

    private fun doCreateCheckout(method: CardMethod, creditInfo: CreditInfo?) {
        collectOne(viewModel.createCheckout(method, creditInfo)) { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    showProgress(false)
                    if (result.data?.isSuccess == true) {
                        // success create checkout
                        Toast.makeText(this, getString(R.string.text_success), Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, result.data?.message.orEmpty(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is ResultWrapper.Loading -> {
                    showProgress(true)
                }

                is ResultWrapper.Failure -> {
                    showProgress(false)
                }

                else -> {}
            }
        }
    }

    private fun showProgress(show: Boolean) {
        binding.incGlobalLoading.root.isVisible = show
    }
}