package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.FlexibleAdapter.Companion.SINGLE
import ji.shop.data.ProductSize
import ji.shop.items.SelectionSizeItemUi

class SelectionSizeItemsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val flexibleAdapter = FlexibleAdapter<SelectionSizeItemUi>(mutableListOf())
        .setMode(SINGLE)
        .addListener { adapter, _, position ->
            if (!adapter.isSelected(position)) {
                adapter.toggleSelection(position)
            }
        }

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = flexibleAdapter
    }

    fun setData(items: List<ProductSize>) {
        flexibleAdapter.clearAdjustSelection()
        flexibleAdapter.addAdjustSelected(0)
        flexibleAdapter.updateDataset(items.map {
            SelectionSizeItemUi(it)
        })
    }

    fun getSelected(): ProductSize? {
        return flexibleAdapter.getSelectedItems().firstOrNull()?.data
    }
}