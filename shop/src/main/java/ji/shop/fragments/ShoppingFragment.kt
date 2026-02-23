package ji.shop.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.Payload
import ji.shop.base.viewBinding
import ji.shop.data.Collection
import ji.shop.data.Product
import ji.shop.data.TabType
import ji.shop.databinding.FragmentShoppingBinding
import ji.shop.dialog.AddProductDialog
import ji.shop.dialog.TurnOnNfcDialog
import ji.shop.exts.collect
import ji.shop.exts.isTablet
import ji.shop.items.CollectionGridItemUi
import ji.shop.items.CollectionLinearItemUi
import ji.shop.items.CountChangOnItemListener
import ji.shop.items.GroupItemUi
import ji.shop.items.ProductItemUi

class ShoppingFragment : BaseFragment(R.layout.fragment_shopping) {
    private val binding by viewBinding(FragmentShoppingBinding::bind)
    private var flexibleCollectionAdapter: FlexibleAdapter<CollectionGridItemUi>? = null
    private var flexibleCollectionSecondaryAdapter: FlexibleAdapter<CollectionLinearItemUi>? = null
    private var flexibleGroupAdapter: FlexibleAdapter<GroupItemUi>? = null
    private var flexibleProductAdapter: FlexibleAdapter<ProductItemUi>? = null

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
            tabViews.setData(
                items = TabType.entries.toList(),
                selectedIndex = shopViewModel.tabTabTypeState.value.ordinal,
                onGetTitle = { tab -> getString(tab.titleRes) }
            ) { selected ->
                shopViewModel.changeTabType(selected)
            }

            btnBackToCollections?.setOnClickListener { shopViewModel.setViewCollection(null) }
            btnNfc.setOnClickListener { doToggleNFC() }
            btnViewCart.setOnClickListener { }
            btnStream.setOnClickListener { }
        }
    }

    private fun initObserves() {
        collect(flow = shopViewModel.myBalanceState) { price ->
            binding.myBalance?.setPrice(price)
        }

        collect(flow = shopViewModel.cartPriceState) { price ->
            doUpdateViewCart(price)
        }

        collect(flow = shopViewModel.tabTabTypeState) { tab ->
            binding.tabViews.setSelected(tab)
        }

        collect(flow = shopViewModel.shopCategoriesFlow) { data ->
            binding.shopCategoryDropDown.setData(data?.first, data?.second)
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

        collectWithProgress(
            stateWrapperView = binding.stateViewDetail,
            flow = shopViewModel.productsFlow
        ) { data ->
            initProducts(data)
        }

        collect(flow = shopViewModel.isNfcEnabledState) {
            doUpdateUiNfc(it)
        }
    }

    private fun doUpdateViewCart(price: String?) {
        if (context.isTablet()) return // skip update btn text
        if (price.isNullOrBlank()) {
            binding.btnViewCart.setText(R.string.text_view_cart)
        } else {
            binding.btnViewCart.text = "${getString(R.string.text_view_cart)} ($price)"
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

            if (context.isTablet()) {
                // select first
                flexibleCollectionAdapter?.toggleSelection(0)
                flexibleCollectionAdapter?.getItem(0)
                    ?.let { shopViewModel.setViewCollection(it.data) }
            }

            binding.recyclerView.adapter = flexibleCollectionAdapter
        }

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
            binding.recyclerViewGroups.adapter = flexibleGroupAdapter
        }
    }

    private fun doUpdateUiSelectedGroup(index: Int) {
        flexibleGroupAdapter?.run {
            if (index == -1) {
                clearSelection()
            } else if (!isSelected(index)) {
                toggleSelection(index)
            }
        }
    }

    private fun initProducts(items: List<ProductItemUi>) {
        flexibleProductAdapter?.updateDataset(items) ?: run {
            flexibleProductAdapter = FlexibleAdapter(items.toMutableList())
                .addListener(object : CountChangOnItemListener {
                    override fun onCountChanged(position: Int, count: Int) {
                        shopViewModel.updateProductCountOfCart(
                            flexibleProductAdapter?.getItem(
                                position
                            )?.data ?: return, count
                        )
                    }

                    override fun onClick(
                        adapter: FlexibleAdapter<*>,
                        view: View,
                        position: Int
                    ) {
                        doAddToCart(
                            position,
                            flexibleProductAdapter?.getItem(position)?.data ?: return
                        )
                    }
                })
            binding.recyclerViewProducts.adapter = flexibleProductAdapter
        }
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

    private fun doToggleNFC() {
        if (shopViewModel.isNfcEnabled()) {
            shopViewModel.setNfcEnabled(false)
        } else {
            TurnOnNfcDialog.Companion.newInstance {
                shopViewModel.setNfcEnabled(true)
            }
                .show(childFragmentManager)
        }
    }

    private fun doUpdateUiNfc(enable: Boolean) {
        binding.btnNfc.isSelected = enable
        if (binding.viewProducts.isVisible) {
            flexibleProductAdapter?.run {
                items.forEach { it.isUseToggleCount = enable }
                notifyItemRangeChanged(0, itemCount, Payload.CHANGE_USE_TOGGLE_COUNT)
            }
        }
    }

    private fun doAddToCart(position: Int, product: Product) {
        AddProductDialog.Companion.newInstance(shopViewModel.getCart(product), product) {
            shopViewModel.addToCart(it)
            flexibleProductAdapter?.run {
                val item = getItem(position) ?: return@run
                item.count = it.count
                notifyItemChanged(position, Payload.CHANGE_COUNT)
            }
        }
            .show(childFragmentManager)
    }
}