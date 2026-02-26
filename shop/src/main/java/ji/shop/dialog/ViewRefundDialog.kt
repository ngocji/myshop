package ji.shop.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.viewBinding
import ji.shop.data.Cart
import ji.shop.databinding.DialogViewRefundBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.RefundItemUi
import kotlin.math.roundToInt

class ViewRefundDialog : BaseDialog(R.layout.dialog_view_refund) {

    private val binding by viewBinding(DialogViewRefundBinding::bind)
    private var items: List<Cart>? = null
    private var flexibleAdapter: FlexibleAdapter<ItemUI<*>>? = null

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
    }

    private fun initViews() {
        with(binding) {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnRefund.setOnClickListener {
                ViewCardInfoDialog.newInstance().show(childFragmentManager)
            }
        }
    }

    private fun initData() {
        val data: MutableList<ItemUI<*>> = items?.map { RefundItemUi.RefundItem(it) }?.toMutableList() ?: mutableListOf()
        flexibleAdapter = FlexibleAdapter(data)
        binding.recyclerView.adapter = flexibleAdapter
    }

    companion object {
        fun newInstance(items: List<Cart>): ViewRefundDialog {
            return ViewRefundDialog().apply {
                this.items = items
            }
        }
    }
}