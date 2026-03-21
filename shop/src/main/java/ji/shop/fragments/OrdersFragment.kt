package ji.shop.fragments

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.START_PAGE
import ji.shop.base.adapter.FlexibleLoadMoreAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.viewBinding
import ji.shop.data.domain.Order
import ji.shop.data.domain.ResultWrapper
import ji.shop.data.domain.WrapPager
import ji.shop.databinding.FragmentOrdersBinding
import ji.shop.dialog.ViewRefundDialog
import ji.shop.exts.collect
import ji.shop.items.OrdersItemUi
import ji.shop.widget.PopupAction
import ji.shop.widget.PopupWindow
import ji.shop.widget.StateWrapperView

class OrdersFragment : BaseFragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private var flexibleOrdersAdapter: FlexibleLoadMoreAdapter<ItemUI<*>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    override fun onRetry() {
        shopViewModel.refreshCollectionsFlow()
    }

    private fun initViews() {
        binding.btnCheckout?.setOnClickListener { }
    }

    private fun initObserves() {
        collect(flow = shopViewModel.orderFlow) { result ->
            if (shopViewModel.isFirstOrderPage()) {
                binding.stateView?.updateStateWithResult(result)
            }

            when (result) {
                is ResultWrapper.Success -> {
                    initOrders(result.data)
                }

                else -> {}
            }
        }
    }

    private fun initOrders(orders: WrapPager<OrdersItemUi>) {
        flexibleOrdersAdapter?.run {
            if (orders.page != START_PAGE) {
                onLoadMoreComplete(orders.items)
                setEnableLoadMore(!orders.isEnded)
            } else {
                updateDataset(orders.items)
                if (orders.items.isEmpty()) {
                    binding.stateView?.updateState(StateWrapperView.State.EMPTY)
                }
            }
        } ?: run {
            flexibleOrdersAdapter =
                FlexibleLoadMoreAdapter<ItemUI<*>>(orders.allItems.toMutableList())
                    .setLoadMoreListener { shopViewModel.loadNextPageOrder() }
                    .apply {
                        addListener { _, view, position ->
                            if (view.id == R.id.img_action) {
                                val order =
                                    (flexibleOrdersAdapter?.getItem(position) as? OrdersItemUi)?.order
                                showActionOrder(order, view)
                            }
                        }
                    }
            if (orders.allItems.isEmpty()) {
                binding.stateView?.updateState(StateWrapperView.State.EMPTY)
            }
        }

        if (binding.rcvInventory.adapter == null) {
            binding.rcvInventory.adapter = flexibleOrdersAdapter
        }
    }

    private fun showActionOrder(order: Order?, view: View) {
        order ?: return
        val popupWindow = PopupWindow(
            requireContext(),
            view,
            object : PopupWindow.PopupWindowListener {
                override fun onActionClick(action: PopupAction) {
                    when (action) {
                        PopupAction.VIEW_ORDER -> {

                        }

                        PopupAction.REFUND -> {
                            ViewRefundDialog.newInstance(order.posOrderId)
                                .show(this@OrdersFragment.childFragmentManager)
                        }

                        PopupAction.COUPONS_REPORT -> {

                        }
                    }
                }
            })
        popupWindow.show()
    }
}