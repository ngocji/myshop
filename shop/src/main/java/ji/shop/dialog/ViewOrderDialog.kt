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
import ji.shop.data.Repo
import ji.shop.data.domain.Order
import ji.shop.data.domain.OrderInfo
import ji.shop.data.domain.ResultWrapper
import ji.shop.data.domain.SummaryViewOrder
import ji.shop.databinding.DialogViewOrderBinding
import ji.shop.exts.collect
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.safeResultFlow
import ji.shop.exts.width
import ji.shop.items.ViewOrderItemUi
import ji.shop.utils.NumberFormater
import kotlin.math.roundToInt

class ViewOrderDialog : BaseDialog(R.layout.dialog_view_order) {
    private val binding by viewBinding(DialogViewOrderBinding::bind)
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
                (it * 0.8).roundToInt()
            }
        )
        window.setGravity(if (isTablet) Gravity.CENTER else Gravity.BOTTOM)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    private fun initViews() {
        with(binding) {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
        }
    }

    private fun initData() {
        collect(flow = safeResultFlow { Repo.getViewOrder(order?.posOrderId) }) { result ->
            binding.stateView.updateStateWithResult(result)
            if (result is ResultWrapper.Success) {
                val viewOrder = result.safeValue()
                val orderItems = viewOrder?.items?.map { ViewOrderItemUi(it) } ?: emptyList()
                updateOrderItems(orderItems)
                updateOrderInfo(viewOrder?.orderInfo)
                updateOrderSummary(viewOrder?.summary)
            }
        }
    }

    private fun updateOrderItems(items: List<ItemUI<*>>) {
        flexibleAdapter = FlexibleAdapter(items.toMutableList())
        binding.recyclerView.adapter = flexibleAdapter
    }

    private fun updateOrderInfo(info: OrderInfo?) {
        info ?: return
        with(binding) {
            tvName.text = info.buyerName
            tvPhone.text = info.buyerPhone
            tvMail.text = info.buyerEmail
            tvTime.text = info.time
            tvPaymentMethod.text = info.paymentMethod
            tvPaid.text = order?.status?.key.orEmpty()
            tvTitle.text = String.format(getString(R.string.text_order), info.posOrderId.orEmpty())
        }
    }

    private fun updateOrderSummary(summary: SummaryViewOrder?) {
        summary ?: return
        with(binding) {
            tvTotalCount.setTitle(
                String.format(
                    getString(R.string.text_format_total_items),
                    summary.itemsCount
                )
            )
            tvSubTotal.setValue(NumberFormater.formatNumberLocale(summary.subtotal))
            tvTax.setValue(NumberFormater.formatNumberLocale(summary.tax))
            tvTotal.setValue(NumberFormater.formatNumberLocale(summary.total))
        }
    }

    companion object {
        fun newInstance(order: Order): ViewOrderDialog {
            return ViewOrderDialog().apply {
                this.order = order
            }
        }
    }
}