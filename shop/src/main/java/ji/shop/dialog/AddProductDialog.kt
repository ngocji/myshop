package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.domain.Cart
import ji.shop.data.domain.Product
import ji.shop.databinding.DialogAddProductBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CountChangOnItemListener
import ji.shop.utils.NumberFormater
import kotlin.math.roundToInt

class AddProductDialog : BaseDialog(R.layout.dialog_add_product) {
    private val binding by viewBinding(DialogAddProductBinding::bind)
    private var currentCart: Cart? = null
    private var product: Product? = null
    private var onAdd: ((Cart) -> Unit)? = null
    override fun doOnWindow(window: Window) {
        super.doOnWindow(window)
        window.setLayout(
            requireActivity().width().let {
                if (context.isTablet()) (it * 0.7).roundToInt() else it
            },
            (requireActivity().height() * 0.9).roundToInt(),
        )
        window.setGravity(Gravity.BOTTOM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initViews() {
        with(binding) {
            toggleCountView?.setListener { doUpdatePrice() }
            selectionSizeItemsView.setListener { _, _, _ -> doUpdatePrice() }
            selectionAdditionalItemsView.setListener(object : CountChangOnItemListener {
                override fun onCountChanged(position: Int, count: Int) {
                    doUpdatePrice()
                }

                override fun onClick(
                    adapter: FlexibleAdapter<*>,
                    view: View,
                    position: Int
                ) {
                }
            })

            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnAddToCart.setOnClickListener { doAddToCart() }
        }
    }

    private fun initData() {
        with(binding) {
            toggleCountView?.setCount(currentCart?.count?.takeIf { it > 0 } ?: 1)
            tvProductName.text = product?.name ?: ""
            imageProduct.load(product?.images?.firstOrNull())
            selectionSizeItemsView.setData(
                items = product?.sizes ?: emptyList(),
                selectedIndex = product?.sizes?.indexOfFirst { it.name == currentCart?.size?.name }
                    .let { if (it == null || it < 0) 0 else it })
            selectionAdditionalItemsView.setData(currentCart, product?.additional ?: emptyList())
            doUpdatePrice()
        }
    }

    private fun doAddToCart() {
        onAdd?.invoke(
            obtainCart() ?: return
        )
        dismissAllowingStateLoss()
    }

    private fun doUpdatePrice() {
        val cart = obtainCart()
        val totalPrice = NumberFormater.formatNumberLocale(cart?.getTotalPrice() ?: 0.0)
        binding.tvTotalPrice?.text = totalPrice
        binding.btnAddToCart.takeIf { context.isTablet() }
            ?.text = "${getString(R.string.text_add_to_cart)} $totalPrice"
    }

    private fun obtainCart(): Cart? {
        val size = binding.selectionSizeItemsView.getSelected()
        val additional = binding.selectionAdditionalItemsView.getMapCount()
        return currentCart?.copy(
            size = size,
            additional = additional
        ) ?: Cart(
            product = product ?: return null,
            size = size,
            additional = additional,
            count = binding.toggleCountView?.currentCount ?: 1
        )
    }

    companion object {
        fun newInstance(cart: Cart?, product: Product, onAdd: (Cart) -> Unit): AddProductDialog {
            return AddProductDialog().apply {
                this.currentCart = cart
                this.product = product
                this.onAdd = onAdd
            }
        }
    }
}