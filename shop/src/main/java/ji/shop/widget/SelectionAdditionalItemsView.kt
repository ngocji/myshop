package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.data.domain.Cart
import ji.shop.data.domain.ProductAdditional
import ji.shop.items.CountChangOnItemListener
import ji.shop.items.SelectionAdditionalItemUi

class SelectionAdditionalItemsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val flexibleAdapter = FlexibleAdapter<SelectionAdditionalItemUi>(mutableListOf())

    init {
        isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = flexibleAdapter
    }

    fun setListener(onItemClickListener: CountChangOnItemListener) {
        flexibleAdapter.addListener(onItemClickListener)
    }

    fun setData(cart: Cart?, items: List<ProductAdditional>) {
        flexibleAdapter.updateDataset(items.map {
            SelectionAdditionalItemUi(it, cart?.additional?.get(it) ?: 0)
        })
    }

    fun getMapCount(): Map<ProductAdditional, Int> {
        return flexibleAdapter.items.associate {
            it.data to it.count
        }
            .filter { it.value > 0 }
    }
}