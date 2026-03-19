package ji.shop.dialog

import android.annotation.SuppressLint
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
import ji.shop.data.domain.ResultWrapper
import ji.shop.databinding.DialogFavoriteBinding
import ji.shop.exts.changeEnabled
import ji.shop.exts.collect
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CountChangOnItemListener
import ji.shop.items.FavoriteProductItemUi
import ji.shop.utils.NumberFormater
import kotlin.math.roundToInt

class FavoritesDialog : BaseDialog(R.layout.dialog_favorite) {
    private val binding by viewBinding(DialogFavoriteBinding::bind)
    private val viewModel by activityViewModels<ShopViewModel>()
    private var flexibleAdapter: FlexibleAdapter<FavoriteProductItemUi>? = null
    private var actionCheckout: ((List<Cart>) -> Unit)? = null
    private val selectedItems = mutableListOf<Cart>()

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
            btnAdd.setOnClickListener {
                dismissAllowingStateLoss()
                actionCheckout?.invoke(selectedItems)
            }
        }
    }

    private fun initData() {
        collect(flow = viewModel.getFavorites()) { results ->
            if (results is ResultWrapper.Success) {
                flexibleAdapter =
                    FlexibleAdapter(results.data.toMutableList())
                        .addListener(object : CountChangOnItemListener {
                            override fun onCountChanged(position: Int, count: Int) {
                                addToCart(flexibleAdapter?.getItem(position), count)
                                doUpdatePrice()
                            }

                            override fun onClick(
                                adapter: FlexibleAdapter<*>,
                                view: View,
                                position: Int
                            ) {
                                doShowAddToCart(flexibleAdapter?.getItem(position))
                            }
                        })
                binding.recyclerView.adapter = flexibleAdapter
                flexibleAdapter?.items?.filter { it.count > 0 }
                    ?.map {
                        Cart(
                            product = it.data,
                            count = it.count
                        )
                    }
                    ?.let { selectedItems.addAll(it) }
                doUpdatePrice()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun doUpdatePrice() {
        if (selectedItems.isEmpty()) {
            binding.titleValuesView.isVisible = false
            binding.btnAdd.changeEnabled(false)
            binding.btnAdd.setText(R.string.text_add_to_cart)
            return
        }

        binding.titleValuesView.isVisible = true
        binding.btnAdd.changeEnabled(true)
        val total = selectedItems.sumOf { it.getTotalPrice() }
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
        binding.btnAdd.text =
            "${getString(R.string.text_add_to_cart)} ${NumberFormater.formatNumberLocale(total + tax)}"
    }

    private fun doShowAddToCart(item: FavoriteProductItemUi?) {
        if (item == null) {
            return
        }
        AddProductDialog.newInstance(
            product = item.data,
            onAdd = { cart ->
                cart.compute(viewModel.shopCategoryState.value)
                val exists = selectedItems.find { it.generatedId == cart.generatedId }
                if (exists != null) {
                    selectedItems.remove(exists)
                }
                selectedItems.add(cart)
                doUpdatePrice()
            })
            .show(childFragmentManager)
    }

    private fun addToCart(item: FavoriteProductItemUi?, count: Int) {
        if (item == null) {
            return
        }
        val exists = selectedItems.find { it.product.id == item.data.id }
        if (exists != null) {
            selectedItems.remove(exists)
        }
        if (count > 0) {
            selectedItems.add(
                Cart(
                    product = item.data,
                    count = count
                )
            )
        }
    }

    companion object {
        fun newInstance(actionCheckout: (List<Cart>) -> Unit): FavoritesDialog {
            return FavoritesDialog().apply {
                this.actionCheckout = actionCheckout
            }
        }
    }
}