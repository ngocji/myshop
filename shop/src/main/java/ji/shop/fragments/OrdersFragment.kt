package ji.shop.fragments

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.OnItemClickListener
import ji.shop.base.viewBinding
import ji.shop.data.domain.Checkout
import ji.shop.databinding.FragmentOrdersBinding
import ji.shop.dialog.ViewRefundDialog
import ji.shop.items.OrdersItemUi
import ji.shop.widget.PopupAction
import ji.shop.widget.PopupWindow
import kotlin.collections.map

class OrdersFragment : BaseFragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private var flexibleOrdersAdapter: FlexibleAdapter<OrdersItemUi>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    private fun initViews() {
        binding.btnCheckout?.setOnClickListener {  }
    }

    private fun initObserves() {
        collectWithProgress(flow = shopViewModel.orderFlow) { data ->
            initOrders(data)
        }
    }

    private fun initOrders(checkout: Checkout) {
        val data = checkout.items.map { OrdersItemUi(it) }
        flexibleOrdersAdapter?.updateDataset(data) ?: run {
            flexibleOrdersAdapter = FlexibleAdapter(data.toMutableList())
                .addListener(object : OnItemClickListener{
                    override fun onClick(
                        adapter: FlexibleAdapter<*>,
                        view: View,
                        position: Int
                    ) {
                        if (view.id == R.id.img_action) {
                            val popupWindow = PopupWindow(
                                view.context,
                                view,
                                object : PopupWindow.PopupWindowListener {
                                    override fun onActionClick(action: PopupAction) {
                                        when (action) {
                                            PopupAction.VIEW_ORDER -> {
                                            }

                                            PopupAction.REFUND -> {
                                                ViewRefundDialog.newInstance(checkout)
                                                    .show(childFragmentManager)
                                            }

                                            PopupAction.COUPONS_REPORT -> {

                                            }
                                        }
                                    }
                                })
                            popupWindow.show()
                        }
                    }

                })
            binding.rcvInventory.adapter = flexibleOrdersAdapter
        }
    }
}