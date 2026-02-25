package ji.shop.fragments

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.databinding.FragmentOrdersBinding
import ji.shop.items.OrdersItemUi

class OrdersFragment : BaseFragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private var flexibleOrdersAdapter: FlexibleAdapter<OrdersItemUi>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    private fun initViews() {

    }

    private fun initObserves() {
        collectWithProgress(flow = shopViewModel.orderFlow) { data ->
            initOrders(data)
        }
    }

    private fun initOrders(items: List<OrdersItemUi>) {
        flexibleOrdersAdapter?.updateDataset(items) ?: run {
            flexibleOrdersAdapter = FlexibleAdapter(items.toMutableList())
            binding.rcvInventory.adapter = flexibleOrdersAdapter
        }
    }
}