package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.activityViewModels
import ji.shop.R
import ji.shop.ShopViewModel
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.domain.Cart
import ji.shop.databinding.DialogEditProductsInCartBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CartItemUi
import ji.shop.items.CountChangOnItemListener
import kotlin.math.roundToInt

class EditProductsInCartDialog : BaseDialog(R.layout.dialog_edit_products_in_cart) {
    private val binding by viewBinding(DialogEditProductsInCartBinding::bind)
    private val viewModel by activityViewModels<ShopViewModel>()
    private var flexibleAdapter: FlexibleAdapter<CartItemUi>? = null
    private var productId = ""
    private var actionAddNew: (() -> Unit)? = null
    private var actionUpdateCarts: ((updated: List<Cart>, removed: List<Cart>) -> Unit)? = null
    private var removedItems = mutableListOf<Cart>()

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
                actionAddNew?.invoke()
            }
        }
    }

    private fun initData() {
        val items = viewModel.getCartItemsByProduct(productId)
            .map { data ->
                CartItemUi(data)
            }
        flexibleAdapter?.updateDataset(items) ?: run {
            flexibleAdapter =
                FlexibleAdapter(items.toMutableList())
                    .addListener(object : CountChangOnItemListener {
                        override fun onCountChanged(position: Int, count: Int): Boolean {
                            updateCarts()
                            return true
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
    }


    private fun updateCarts() {
        val newItems = obtainItems()
        actionUpdateCarts?.invoke(newItems, removedItems)
    }

    private fun doModifyItem(item: Cart?, position: Int) {
        item ?: return
        var updatedItem = item
        flexibleAdapter?.run {
            val itemUI = getItem(position)
            updatedItem = item.copy(count = itemUI?.count ?: 0)
        }

        AddProductDialog.newInstance(
            currentCart = updatedItem,
            product = item.product,
            onAdd = { cart, cartId ->
                flexibleAdapter?.run {
                    cart.compute(viewModel.shopCategoryState.value)
                    if (cart.generatedId == cartId) {
                        // change count
                        setItem(position, CartItemUi(data = cart))
                        updateCarts()
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
                                removeItem(prevIndex)?.data?.also {
                                    removedItems.add(it)
                                }
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

                    updateCarts()
                }
            }
        )
            .show(childFragmentManager)
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

    companion object {
        fun newInstance(
            productId: String,
            actionUpdateCarts: (updated: List<Cart>, removed: List<Cart>) -> Unit,
            actionAddNew: () -> Unit
        ): EditProductsInCartDialog {
            return EditProductsInCartDialog().apply {
                this.productId = productId
                this.actionUpdateCarts = actionUpdateCarts
                this.actionAddNew = actionAddNew
            }
        }
    }
}