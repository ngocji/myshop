package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.items.OrderAvatarItemUi

class OrderAvatarItemsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val flexibleAdapter = FlexibleAdapter<OrderAvatarItemUi>(mutableListOf())

    init {
        isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        adapter = flexibleAdapter
    }

    fun setData(items: List<Any?>) {
        flexibleAdapter.updateDataset(items.take(4).map {
            OrderAvatarItemUi(it)
        })
    }
}