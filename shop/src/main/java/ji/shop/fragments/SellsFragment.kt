package ji.shop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.domain.Group.Companion.isOnlyItem
import ji.shop.databinding.FragmentSellsBinding
import ji.shop.dialog.FavoritesDialog
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
            btnBackToCollections?.setOnClickListener {
                shopViewModel.setViewCollection(null)
                shopViewModel.setViewGroup(null)
            }
            btnViewCart?.setOnClickListener { shopViewModel.viewCart() }
            btnFavorites?.setOnClickListener { doViewFavorites() }
        }
    }

    private fun initObserves() {
        collectWithProgress(flow = shopViewModel.sellDataState) {
            // update ui mode
        }

        collect(flow = shopViewModel.cartPriceState) { price ->
            doUpdateViewCart(price)
        }

        collect(flow = shopViewModel.collectionsFlow) {
            initCollections(it)
        }

        collect(flow = shopViewModel.collectionState) { data ->
        }

        collect(flow = shopViewModel.groupsFlow) { data ->
            initGroups(data)
        }

        collect(flow = shopViewModel.groupSelectedIndexFlow) { data ->
            doUpdateUiSelectedGroup(data)
        }

        collect(flow = shopViewModel.showDetailFlow) { show ->
            doShowDetailUI(show)
        }
    }

    @SuppressLint("SetTextI18n")
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
                .setMode(FlexibleAdapter.SINGLE)
                .addListener { adapter, _, position ->
                    if (!adapter.isSelected(position)) {
                        if (context.isTablet()) {
                            adapter.toggleSelection(position)
                        }

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
        binding.recyclerviewSecondaryCollections?.isVisible = data.second.isNotEmpty()
        binding.btnBackToCollections?.isVisible = data.second.isNotEmpty()
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

        val isOnlyItem = items.firstOrNull()?.data?.isOnlyItem() == true
        binding.recyclerViewGroups.isVisible = !isOnlyItem
        binding.viewPager.setBackgroundResource(
            if (isOnlyItem) R.drawable.bg_product_container_round_all else R.drawable.bg_product_container
        )

        binding.viewPager.unregisterOnPageChangeCallback(callbackChangePager)
        binding.viewPager.registerOnPageChangeCallback(callbackChangePager)
        binding.viewPager.adapter = SellsPagerAdapter(this, items)
    }

    private fun doUpdateUiSelectedGroup(index: Int) {
        flexibleGroupAdapter?.run {
            if (!isSelected(index)) {
                toggleSelection(index)
            }
        }
        binding.viewPager.setCurrentItem(index, false)
    }

    private fun doShowDetailUI(showDetail: Boolean) {
        if (context.isTablet()) {
            return
        }
        // need update phone
        val viewProducts = binding.viewProducts
        if (showDetail) {
            if (!viewProducts.isVisible) {
                viewProducts.alpha = 0f
                viewProducts.isVisible = true
                viewProducts
                    .animate()
                    .alpha(1f)
                    .start()
            }
        } else {
            if (viewProducts.isVisible) {
                viewProducts
                    .animate()
                    .alpha(0f)
                    .withEndAction { viewProducts.isVisible = false }
                    .start()
            }
        }

    }

    private fun doViewFavorites() {
        FavoritesDialog.newInstance { items ->
            shopViewModel.addToCarts(items, true)
        }
            .show(childFragmentManager)
    }
}