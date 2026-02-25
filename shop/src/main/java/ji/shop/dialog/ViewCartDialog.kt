package ji.shop.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.Cart
import ji.shop.databinding.DialogViewCartBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CartItemUi
import ji.shop.items.CountChangOnItemListener
import ji.shop.utils.NumberFormater
import kotlin.math.roundToInt

class ViewCartDialog : BaseDialog(R.layout.dialog_view_cart) {
    private val binding by viewBinding(DialogViewCartBinding::bind)
    private var flexibleAdapter: FlexibleAdapter<CartItemUi>? = null
    private var items: List<Cart>? = null
    private var actionCheckout: ((List<Cart>, Boolean) -> Unit)? = null
    private var isGotoCheckout=false

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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val newItems = obtainItems()
        actionCheckout?.invoke(newItems, isGotoCheckout)
    }

    private fun initViews() {
        with(binding) {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnCheckout.setOnClickListener {
                isGotoCheckout=true
                dismissAllowingStateLoss()
            }
        }
    }

    private fun initData() {
        flexibleAdapter =
            FlexibleAdapter(items?.map { CartItemUi(it) }?.toMutableList() ?: mutableListOf())
                .addListener(object : CountChangOnItemListener {
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
        actionCheckout?.invoke(items, false)
    }

    companion object {
        fun newInstance(items: List<Cart>, actionCheckout: (List<Cart>, Boolean) -> Unit): ViewCartDialog {
            return ViewCartDialog().apply {
                this.items = items
                this.actionCheckout = actionCheckout
            }
        }
    }
}