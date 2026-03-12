package ji.shop.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.Cart
import ji.shop.data.Repo
import ji.shop.databinding.DialogFavoriteBinding
import ji.shop.exts.collect
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CartItemUi
import ji.shop.items.CountChangOnItemListener
import ji.shop.utils.NumberFormater
import kotlin.math.roundToInt

class FavoritesDialog : BaseDialog(R.layout.dialog_favorite) {
    private val binding by viewBinding(DialogFavoriteBinding::bind)
    private var flexibleAdapter: FlexibleAdapter<CartItemUi>? = null
    private var actionCheckout: ((List<Cart>) -> Unit)? = null
    private var selectedItems = emptyList<Cart>()

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
        collect(flow = Repo.getFavorites()) { items ->
            flexibleAdapter =
                FlexibleAdapter(items.map { CartItemUi(it) }.toMutableList())
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

    @SuppressLint("SetTextI18n")
    private fun doUpdatePrice() {
        selectedItems = obtainItems()
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

    companion object {
        fun newInstance(actionCheckout: (List<Cart>) -> Unit): FavoritesDialog {
            return FavoritesDialog().apply {
                this.actionCheckout = actionCheckout
            }
        }
    }
}