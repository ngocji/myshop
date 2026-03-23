package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.core.view.isVisible
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.viewBinding
import ji.shop.data.Repo
import ji.shop.data.domain.OrderInfo
import ji.shop.data.domain.Refund
import ji.shop.data.domain.RefundItem
import ji.shop.data.domain.ResultWrapper
import ji.shop.databinding.DialogViewRefundBinding
import ji.shop.exts.changeEnabled
import ji.shop.exts.collect
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.safeResultFlow
import ji.shop.exts.width
import ji.shop.items.RefundItemUi
import ji.shop.items.RefundItemUi.Companion.PAYLOAD_CHANGE_COUNT
import ji.shop.items.TotalRefundItemUi
import ji.shop.items.TotalRefundItemUi.Companion.PAYLOAD_CHANGE_TOTAL
import ji.shop.widget.StateWrapperView
import kotlin.math.roundToInt

class ViewRefundDialog : BaseDialog(R.layout.dialog_view_refund) {
    private val binding by viewBinding(DialogViewRefundBinding::bind)
    private var postOrderId: String? = null
    private var refund: Refund? = null
    private var actionRefund: ((refund: Refund, selectedItems: List<RefundItem>) -> Unit)? = null
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

    private fun initViews() {
        with(binding) {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnRefund.setOnClickListener {
                actionRefund?.invoke(
                    refund ?: return@setOnClickListener run { dismissAllowingStateLoss() },
                    flexibleAdapter?.items
                        ?.filterIsInstance<RefundItemUi>()
                        ?.filter { it.count > 0 }
                        ?.map {
                            it.data.copy(
                                refundQuantity = it.count
                            )
                        } ?: emptyList())
                dismissAllowingStateLoss()
            }
        }
    }

    private fun initData() {
        collect(flow = safeResultFlow { Repo.getRefund(postOrderId) }) { result ->
            binding.stateView.updateStateWithResult(result)
            if (result is ResultWrapper.Success) {
                refund = result.safeValue()
                val refundItems = result.safeValue()?.items.orEmpty()
                    .map { RefundItemUi(it, count = it.quantityRefundable) }

                val items = buildList {
                    addAll(refundItems)
                    if (isNotEmpty()) {
                        add(TotalRefundItemUi(refundItems.sumOf { it.data.unitPrice * it.count }))
                    }
                }
                updateItems(items)
                updateCustomer(refund?.order)
            }
        }
    }

    private fun updateItems(items: List<ItemUI<*>>) {
        if (items.isEmpty()) {
            binding.stateView.updateState(StateWrapperView.State.EMPTY)
            binding.btnRefund.changeEnabled(false)
            return
        }
        binding.content.isVisible = true
        binding.btnRefund.changeEnabled(true)
        flexibleAdapter?.updateDataset(items) ?: run {
            flexibleAdapter = FlexibleAdapter(items.toMutableList())
                .addListener { adapter, view, position ->
                    changeCount(view.findViewById(R.id.fl_count) ?: view, position)
                }
        }
        binding.recyclerView.adapter = flexibleAdapter
    }

    private fun changeCount(view: View, position: Int) {
        val item = flexibleAdapter?.getItem(position) as? RefundItemUi ?: return
        val counts = buildList {
            repeat(item.data.quantityRefundable + 1) {
                add(it)
            }
        }
        SelectionDropdownPopup(
            context = requireContext(),
            items = counts,
            onSelectedItem = {
                item.count = it
                flexibleAdapter?.notifyItemChanged(position, PAYLOAD_CHANGE_COUNT)
                doUpdateTotalRefund()
            },
        )
            .show(
                view,
                targetHeight = -2,
                xOff = -view.measuredWidth,
                gravity = Gravity.END
            )
    }

    private fun updateCustomer(customerInfo: OrderInfo?) {
        customerInfo ?: return
        with(binding) {
            tvName.text = customerInfo.buyerName
            tvPhone.text = customerInfo.buyerPhone
            tvMail.text = customerInfo.buyerEmail
        }
    }

    private fun doUpdateTotalRefund() {
        flexibleAdapter?.run {
            val price = items.sumOf {
                if (it is RefundItemUi) {
                    it.data.unitPrice * it.count
                } else {
                    0.0
                }
            }

            (getItem(itemCount - 1) as? TotalRefundItemUi)?.let {
                it.totalPrice = price
                notifyItemChanged(itemCount - 1, PAYLOAD_CHANGE_TOTAL)
            }
        }
    }

    companion object {
        fun newInstance(
            postOrderId: String?,
            action: (refund: Refund, selectedItems: List<RefundItem>) -> Unit
        ): ViewRefundDialog {
            return ViewRefundDialog().apply {
                this.postOrderId = postOrderId
                this.actionRefund = action
            }
        }
    }
}