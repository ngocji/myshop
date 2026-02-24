package ji.shop.fragments

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.databinding.FragmentInventoryBinding
import ji.shop.exts.collect
import ji.shop.items.InventoryUi

class InventoryFragment : BaseFragment(R.layout.fragment_inventory) {
    private val binding by viewBinding(FragmentInventoryBinding::bind)
    private var flexibleInventoryAdapter: FlexibleAdapter<InventoryUi>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    private fun initViews() {

    }

    private fun initObserves() {
        collect(flow = shopViewModel.inventoriesFlow) { data ->
            initInventories(data ?: emptyList())
        }
    }

    private fun initInventories(items: List<InventoryUi>) {
        flexibleInventoryAdapter?.updateDataset(items) ?: run {
            flexibleInventoryAdapter = FlexibleAdapter(items.toMutableList())
            binding.rcvInventory.adapter = flexibleInventoryAdapter
        }
    }
}