package ji.shop.fragments

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.FlexibleAdapter.Companion.SINGLE
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
        binding.btnCheckout?.setOnClickListener {  }
    }

    private fun initObserves() {
        collectWithProgress(flow = shopViewModel.inventoriesFlow) { data ->
            initInventories(data)
        }
    }

    private fun initInventories(items: List<InventoryUi>) {
        flexibleInventoryAdapter?.updateDataset(items) ?: run {
            flexibleInventoryAdapter = FlexibleAdapter(items.toMutableList())
                .setMode(SINGLE)
                .addListener { adapter, view, position ->
                    if (view.id == R.id.img_Visibility) {
                        adapter.toggleSelection(position)
                    }
                }
            binding.rcvInventory.adapter = flexibleInventoryAdapter
        }
    }
}