package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.viewBinding
import ji.shop.data.Repo
import ji.shop.data.domain.Order
import ji.shop.data.domain.ResultWrapper
import ji.shop.databinding.DialogOrderDetailItemsBinding
import ji.shop.exts.collect
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.safeResultFlow
import ji.shop.exts.width
import ji.shop.items.ViewOrderItemUi
import ji.shop.widget.StateWrapperView
import kotlin.math.roundToInt

class OrderDetailItemsDialog : BaseDialog(R.layout.dialog_order_detail_items) {
    private val binding by viewBinding(DialogOrderDetailItemsBinding::bind)
    private var order: Order? = null
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
                if (isTablet) (it * 0.7).roundToInt() else it
            },
            requireActivity().height().let {
                (it * 0.6).roundToInt()
            }
        )
        window.setGravity(if (isTablet) Gravity.CENTER else Gravity.BOTTOM)
    }

    private fun initViews() {
        with(binding) {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
        }
    }

    private fun initData() {
        collect(flow = safeResultFlow { Repo.getOrderDetailItems(order?.posOrderId) }) { result ->
            binding.stateView.updateStateWithResult(result)
            if (result is ResultWrapper.Success) {
                val orderItems = result.safeValue()?.map { ViewOrderItemUi(it) } ?: emptyList()
                updateOrderItems(orderItems)
            }
        }
    }

    private fun updateOrderItems(items: List<ItemUI<*>>) {
        if (items.isEmpty()) {
            binding.stateView.updateState(StateWrapperView.State.EMPTY)
        }
        flexibleAdapter = FlexibleAdapter(items.toMutableList())
        binding.recyclerView.adapter = flexibleAdapter
    }

    companion object {
        fun newInstance(order: Order): OrderDetailItemsDialog {
            return OrderDetailItemsDialog().apply {
                this.order = order
            }
        }
    }
}