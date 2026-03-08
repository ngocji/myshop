package ji.shop.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.Collection
import ji.shop.databinding.FragmentSellsBinding
import ji.shop.exts.collect
import ji.shop.exts.isTablet
import ji.shop.fragments.items.SellsPagerAdapter
import ji.shop.items.CollectionGridItemUi
import ji.shop.items.CollectionLinearItemUi
import ji.shop.items.GroupItemUi

class SellsFragment : BaseFragment(R.layout.fragment_sells) {
    private val binding by viewBinding(FragmentSellsBinding::bind)
    private var flexibleCollectionAdapter: FlexibleAdapter<CollectionGridItemUi>? = null
    private var flexibleCollectionSecondaryAdapter: FlexibleAdapter<CollectionLinearItemUi>? = null
    private var flexibleGroupAdapter: FlexibleAdapter<GroupItemUi>? = null
    private val callbackChangePager = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            shopViewModel.setViewGroup(
                flexibleGroupAdapter?.getItem(position)?.data ?: return
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    override fun onRetry() {
        if (context.isTablet()) {
            // need to  refresh all
        } else {
            shopViewModel.refreshCollectionsFlow()
        }
    }

    private fun initViews() {
        with(binding) {
            btnBackToCollections?.setOnClickListener { shopViewModel.setViewCollection(null) }
            btnViewCart?.setOnClickListener { shopViewModel.viewCart() }
        }
    }

    private fun initObserves() {
        collect(flow = shopViewModel.cartPriceState) { price ->
            doUpdateViewCart(price)
        }

        collectWithProgress(flow = shopViewModel.collectionsFlow) {
            initCollections(it)
        }

        collect(flow = shopViewModel.collectionState) { data ->
            doUpdateUiViewCollection(data)
        }

        collect(flow = shopViewModel.groupsFlow) { data ->
            initGroups(data)
        }

        collect(flow = shopViewModel.groupSelectedIndexFlow) { data ->
            doUpdateUiSelectedGroup(data)
        }
    }

    private fun doUpdateViewCart(price: String?) {
        if (context.isTablet()) return // skip update btn text
        if (price.isNullOrBlank()) {
            binding.btnViewCart?.setText(R.string.text_view_cart)
        } else {
            binding.btnViewCart?.text = "${getString(R.string.text_view_cart)} ($price)"
        }
    }

    private fun initCollections(data: Pair<List<CollectionGridItemUi>, List<CollectionLinearItemUi>>) {
        // grid
        flexibleCollectionAdapter?.updateDataset(data.first) ?: run {
            flexibleCollectionAdapter = FlexibleAdapter(data.first.toMutableList())
                .setMode(FlexibleAdapter.Companion.SINGLE)
                .addListener { adapter, _, position ->
                    if (!adapter.isSelected(position)) {
                        adapter.toggleSelection(position)

                        flexibleCollectionAdapter?.getItem(position)
                            ?.let { shopViewModel.setViewCollection(it.data) }
                    }
                }
        }

        if (context.isTablet()) {
            val index =
                data.first.indexOfFirst { it.data.id == shopViewModel.collectionState.value?.id }
                    .takeIf { it > -1 } ?: 0
            if (flexibleCollectionAdapter?.isSelected(index) == false) {
                flexibleCollectionAdapter?.toggleSelection(index)
            }
            if (shopViewModel.collectionState.value == null) {
                flexibleCollectionAdapter?.getItem(index)
                    ?.let { shopViewModel.setViewCollection(it.data) }
            }
        }

        binding.recyclerView.adapter = flexibleCollectionAdapter

        // linear if has
        flexibleCollectionSecondaryAdapter?.updateDataset(data.second) ?: run {
            flexibleCollectionSecondaryAdapter = FlexibleAdapter(data.second.toMutableList())
                .setMode(FlexibleAdapter.Companion.SINGLE)
                .addListener { adapter, _, position ->
                    if (!adapter.isSelected(position)) {
                        adapter.toggleSelection(position)
                        flexibleCollectionSecondaryAdapter?.getItem(position)
                            ?.let { shopViewModel.setViewCollection(it.data) }
                    }
                }
        }
        binding.recyclerviewSecondaryCollections?.adapter = flexibleCollectionSecondaryAdapter
    }

    private fun initGroups(items: List<GroupItemUi>) {
        flexibleGroupAdapter?.updateDataset(items) ?: run {
            flexibleGroupAdapter = FlexibleAdapter(items.toMutableList())
                .setMode(FlexibleAdapter.SINGLE)
                .addListener { adapter, _, position ->
                    if (!adapter.isSelected(position)) {
                        adapter.toggleSelection(position)
                        shopViewModel.setViewGroup(
                            flexibleGroupAdapter?.getItem(position)?.data ?: return@addListener
                        )
                    }
                }
        }

        val index = items.indexOfFirst { it.data.id == shopViewModel.groupState.value?.id }
            .takeIf { it > -1 } ?: 0
        if (flexibleGroupAdapter?.isSelected(index) == false) {
            flexibleGroupAdapter?.toggleSelection(index)
        }
        binding.recyclerViewGroups.itemAnimator = null
        binding.recyclerViewGroups.adapter = flexibleGroupAdapter
        binding.viewPager.unregisterOnPageChangeCallback(callbackChangePager)
        binding.viewPager.registerOnPageChangeCallback(callbackChangePager)
        binding.viewPager.adapter = SellsPagerAdapter(this, items)
    }

    private fun doUpdateUiSelectedGroup(index: Int) {
        flexibleGroupAdapter?.run {
            if (index == -1) {
                clearSelection()
            } else if (!isSelected(index)) {
                toggleSelection(index)
            }
        }
        binding.viewPager.setCurrentItem(index, false)
    }

    private fun doUpdateUiViewCollection(collection: Collection?) {
        if (context.isTablet()) {
            return
        }
        // need update phone
        val viewProducts = binding.viewProducts
        if (collection == null) {
            // revert
            if (viewProducts.isVisible) {
                viewProducts
                    .animate()
                    .alpha(0f)
                    .withEndAction { viewProducts.isVisible = false }
                    .start()
            }
        } else {
            if (!viewProducts.isVisible) {
                viewProducts.alpha = 0f
                viewProducts.isVisible = true
                viewProducts
                    .animate()
                    .alpha(1f)
                    .start()
            }
        }
    }
}