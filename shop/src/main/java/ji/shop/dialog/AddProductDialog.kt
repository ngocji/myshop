package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.viewBinding
import ji.shop.data.Cart
import ji.shop.data.Product
import ji.shop.databinding.DialogAddProductBinding
import kotlin.math.roundToInt

class AddProductDialog : BaseDialog(R.layout.dialog_add_product) {
    private val binding by viewBinding(DialogAddProductBinding::bind)
    private var currentCart: Cart? = null
    private var product: Product? = null
    private var onAdd: ((Cart) -> Unit)? = null
    override fun doOnWindow(window: Window) {
        super.doOnWindow(window)
        window.setLayout(
            resources.displayMetrics.widthPixels,
            (resources.displayMetrics.heightPixels * 0.9).roundToInt(),
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
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnAdd.setOnClickListener { doAddToCart() }
        }
    }

    private fun initData() {
        with(binding) {
            tvProductName.text = product?.name ?: ""
            imageProduct.load(product?.images?.firstOrNull())
            selectionSizeItemsView.setData(
                items = product?.sizes ?: emptyList(),
                selectedIndex = product?.sizes?.indexOfFirst { it.name == currentCart?.size?.name }
                    .let { if (it == null || it < 0) 0 else it })
            selectionAdditionalItemsView.setData(currentCart, product?.additional ?: emptyList())
        }
    }

    private fun doAddToCart() {
        val size = binding.selectionSizeItemsView.getSelected() ?: return
        val additional = binding.selectionAdditionalItemsView.getMapCount()
        onAdd?.invoke(
            currentCart?.copy(
                size = size,
                additional = additional
            ) ?: Cart(
                product = product ?: return,
                size = size,
                additional = additional,
                count = 1
            )
        )
        dismissAllowingStateLoss()
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