package ji.shop.fragments.items

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import ji.shop.R
import ji.shop.base.BaseFragment
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.Payload
import ji.shop.base.viewBinding
import ji.shop.data.domain.Product
import ji.shop.databinding.FragmentSellsProductsItemBinding
import ji.shop.dialog.AddProductDialog
import ji.shop.exts.collect
import ji.shop.items.CountChangOnItemListener
import ji.shop.items.ProductItemUi

class ProductsItemFragment : BaseFragment(R.layout.fragment_sells_products_item) {
    private val binding by viewBinding(FragmentSellsProductsItemBinding::bind)
    private val collectionId by lazy {
        arguments?.getString(EXTRA_COLLECTION_ID).orEmpty()
    }
    private val groupId by lazy {
        arguments?.getString(EXTRA_GROUP_ID).orEmpty()
    }

    private var flexibleProductAdapter: FlexibleAdapter<ProductItemUi>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    private fun initViews() {}

    private fun initObserves() {
        collect(flow = shopViewModel.getProductsFlow(collectionId, groupId)) { data ->
            initProducts(data)
        }

        collect(flow = shopViewModel.getProductsCountNotifyFlow(collectionId, groupId)) {
            doUpdateProductCountUi(it)
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
            binding.recyclerViewProducts.itemAnimator = null
            binding.recyclerViewProducts.adapter = flexibleProductAdapter
        }
    }

    private fun doUpdateProductCountUi(range: Pair<Int?, Int?>) {
        val start = range.first ?: return
        val end = range.second ?: return
        flexibleProductAdapter?.run {
            if (start == end) {
                notifyItemChanged(start, Payload.CHANGE_COUNT)
            } else {
                notifyItemRangeChanged(start, end + 1, Payload.CHANGE_COUNT)
            }
        }
    }

    private fun doAddToCart(position: Int, product: Product) {
        AddProductDialog.newInstance(shopViewModel.getCart(product), product) {
            shopViewModel.addToCart(it)
            flexibleProductAdapter?.run {
                val item = getItem(position) ?: return@run
                item.count = it.count
                notifyItemChanged(position, Payload.CHANGE_COUNT)
            }
        }
            .show(childFragmentManager)
    }

    companion object {
        private const val EXTRA_COLLECTION_ID = "collection_id"
        private const val EXTRA_GROUP_ID = "extra_group_id"
        fun newInstance(collectionId: String, groupId: String): ProductsItemFragment {
            return ProductsItemFragment().apply {
                arguments = bundleOf(
                    EXTRA_COLLECTION_ID to collectionId,
                    EXTRA_GROUP_ID to groupId
                )
            }
        }
    }
}