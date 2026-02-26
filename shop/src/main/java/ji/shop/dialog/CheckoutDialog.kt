package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.viewBinding
import ji.shop.data.Cart
import ji.shop.databinding.DialogViewCheckoutBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.utils.NumberFormater
import kotlin.math.roundToInt

class CheckoutDialog : BaseDialog(R.layout.dialog_view_checkout) {
    private val binding by viewBinding(DialogViewCheckoutBinding::bind)
    private var items: List<Cart>? = null

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
            btnCash.setOnClickListener {

            }
            btnCredit.setOnClickListener { }
            btnAddCustomerInfo.setOnClickListener { }
        }
    }

    private fun initData() {
        with(binding) {
            tvTotalItems.text = getString(R.string.text_format_total_items, items?.size ?: 0)
            val totalPrice = items?.sumOf { it.getTotalPrice() } ?: 0.0
            val tax = totalPrice * 0.038f
            tvTax.text = NumberFormater.formatNumberLocale(tax)
            tvTotal.text = NumberFormater.formatNumberLocale(totalPrice)
        }
    }

    companion object {
        fun newInstance(items: List<Cart>): CheckoutDialog {
            return CheckoutDialog().apply {
                this.items = items
            }
        }
    }
}