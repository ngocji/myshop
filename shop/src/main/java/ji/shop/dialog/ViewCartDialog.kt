package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import ji.shop.R
import ji.shop.ShopViewModel
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.domain.Cart
import ji.shop.databinding.DialogViewCartBinding
import ji.shop.exts.changeEnabled
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CartItemUi
import ji.shop.items.CountChangOnItemListener
import ji.shop.utils.NumberFormater
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.roundToInt

class ViewCartDialog : BaseDialog(R.layout.dialog_view_cart) {
    private val binding by viewBinding(DialogViewCartBinding::bind)
    private val shopViewModel by activityViewModels<ShopViewModel>()
    private var flexibleAdapter: FlexibleAdapter<CartItemUi>? = null
    private var actionCheckout: ((cart: List<Cart>) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    override fun doOnWindow(window: Window) {
        super.doOnWindow(window)
        val isTablet = context.isTablet()
        window.setLayout(
            requireActivity().width().let {
                if (isTablet) (it * 0.4).roundToInt() else it
            },
            requireActivity().height().let {
                if (isTablet) it else (it * 0.7).roundToInt()
            }
        )
        window.setGravity(if (isTablet) Gravity.END else Gravity.BOTTOM)
    }

    private fun initViews() {
        with(binding) {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnCheckout.setOnClickListener {
                actionCheckout?.invoke(obtainItems())
                dismissAllowingStateLoss()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initData() {
        val items = shopViewModel.getCartItems()
            .map { data ->
                CartItemUi(data)
            }
        flexibleAdapter?.updateDataset(items) ?: run {
            flexibleAdapter =
                FlexibleAdapter(items.toMutableList())
                    .addListener(object : CountChangOnItemListener {
                        override fun onCountChanged(position: Int, count: Int) {
                            doUpdatePrice()
                        }

                        override fun onClick(
                            adapter: FlexibleAdapter<*>,
                            view: View,
                            position: Int
                        ) {
                            doModifyItem(flexibleAdapter?.getItem(position)?.data, position)
                        }
                    })
        }
        binding.recyclerView.adapter = flexibleAdapter
        doUpdatePrice()
    }

    private fun obtainItems(): List<Cart> {
        return flexibleAdapter?.items?.mapNotNull { item ->
            if (item.count > 0) {
                item.data.copy(count = item.count)
            } else {
                null
            }
        } ?: emptyList()
    }

    private fun doUpdatePrice() {
        val items = obtainItems()
        shopViewModel.updateCarts(items)

        if (items.isEmpty()) {
            binding.titleValuesView.isVisible = false
            binding.btnCheckout.changeEnabled(false)
            return
        }
        binding.titleValuesView.isVisible = true
        binding.btnCheckout.changeEnabled(true)
        val total = items.sumOf { it.getTotalPrice() }
        val tax = total * 0.038f
        binding.titleValuesView.setData(
            Pair(
                R.string.text_subtotal,
                NumberFormater.formatNumberLocale(total)
            ),
            Pair(
                R.string.text_tax,
                NumberFormater.formatNumberLocale(tax)
            )
        )
    }

    private fun doModifyItem(item: Cart?, position: Int) {
        item ?: return
        AddProductDialog.newInstance(
            currentCart = item,
            product = item.product,
            onAdd = { cart, cartId ->
                flexibleAdapter?.run {
                    cart.compute(shopViewModel.shopCategoryState.value)
                    if (cart.generatedId == cartId) {
                        // nothing changed
                        return@run
                    }
                    val prevIndex = items.indexOfFirst { it.data.generatedId == cartId }
                    val existsIndex = items.indexOfFirst { it.data.generatedId == cart.generatedId }
                    when {
                        existsIndex != -1 -> {
                            // merge
                            val newItem = items[existsIndex].data.let { ext ->
                                ext.copy(
                                    count = ext.count + cart.count
                                )
                            }
                            setItem(existsIndex, CartItemUi(data = newItem))
                            if (prevIndex != -1) {
                                removeItem(prevIndex)
                            }
                        }

                        prevIndex != -1 -> {
                            // replace
                            setItem(prevIndex, CartItemUi(data = cart))
                        }

                        else -> {
                            // add new
                            addItem(CartItemUi(data = cart))
                        }
                    }

                    doUpdatePrice()
                }
            }
        )
            .show(childFragmentManager)
    }


    companion object {
        fun newInstance(actionCheckout: (cart: List<Cart>) -> Unit): ViewCartDialog {
            return ViewCartDialog().apply {
                this.actionCheckout = actionCheckout
            }
        }
    }
}