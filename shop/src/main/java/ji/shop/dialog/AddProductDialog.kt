package ji.shop.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.core.view.isVisible
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
    private var product: Product? = null
    private var onAdd: ((Cart) -> Unit)? = null
    private val onCountChangedListener = object : CountChangOnItemListener {
        override fun onCountChanged(position: Int, count: Int) {
            doUpdatePrice()
        }

        override fun onClick(
            adapter: FlexibleAdapter<*>,
            view: View,
            position: Int
        ) {
            doUpdatePrice()
        }
    }

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

            selectionVariationItemsView.setListener(onCountChangedListener)
            groupModifiersView.setListener(onCountChangedListener)

            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnAddToCart.setOnClickListener { doAddToCart() }
        }
    }

    private fun initData() {
        with(binding) {
            toggleCountView?.setCount(1)
            tvProductName.text = product?.name ?: ""
            imageProduct.load(product?.images?.firstOrNull())

            groupVariationsView.isVisible = !product?.variations.isNullOrEmpty()
            selectionVariationItemsView.setData(items = product?.variations ?: emptyList())

            groupModifiersView.setData(product?.modifiers ?: emptyList())
            doUpdatePrice()
        }
    }

    private fun doAddToCart() {
        onAdd?.invoke(
            obtainCart() ?: return
        )
        dismissAllowingStateLoss()
    }

    @SuppressLint("SetTextI18n")
    private fun doUpdatePrice() {
        val cart = obtainCart()
        val totalPrice = NumberFormater.formatNumberLocale(cart?.getTotalPrice() ?: 0.0)
        binding.tvTotalPrice?.text = totalPrice
        binding.btnAddToCart.takeIf { context.isTablet() }
            ?.text = "${getString(R.string.text_add_to_cart)} $totalPrice"
    }

    private fun obtainCart(): Cart? {
        val size = binding.selectionVariationItemsView.getSelected()
        val modifiers = binding.groupModifiersView.getSelectedData()
        return Cart(
            product = product ?: return null,
            variation = size,
            modifiers = modifiers,
            count = binding.toggleCountView?.currentCount ?: 1
        )
    }

    companion object {
        fun newInstance(product: Product, onAdd: (Cart) -> Unit): AddProductDialog {
            return AddProductDialog().apply {
                this.product = product
                this.onAdd = onAdd
            }
        }
    }
}