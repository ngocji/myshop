package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.data.Cart
import ji.shop.data.ProductAdditional
import ji.shop.items.SelectionAdditionalItemUi

class SelectionAdditionalItemsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val flexibleAdapter = FlexibleAdapter<SelectionAdditionalItemUi>(mutableListOf())

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = flexibleAdapter
    }

    fun setData(cart: Cart, items: List<ProductAdditional>) {
        flexibleAdapter.updateDataset(items.map {
            SelectionAdditionalItemUi(it, cart.additional[it] ?: 0)
        })
    }

    fun getMapCount(): Map<ProductAdditional, Int> {
        return flexibleAdapter.items.associate {
            it.data to it.count
        }
            .filter { it.value > 0 }
    }
}