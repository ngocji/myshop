package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.FlexibleAdapter.Companion.SINGLE
import ji.shop.base.adapter.OnItemClickListener
import ji.shop.data.domain.ProductSize
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
        isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = flexibleAdapter
    }

    fun setListener(onItemClickListener: OnItemClickListener) {
        flexibleAdapter.addListener(onItemClickListener)
    }

    fun setData(items: List<ProductSize>, selectedIndex: Int = 0) {
        flexibleAdapter.clearAdjustSelection()
        flexibleAdapter.updateDataset(items.map {
            SelectionSizeItemUi(it)
        })
        if (!flexibleAdapter.isSelected(selectedIndex)) {
            flexibleAdapter.toggleSelection(selectedIndex)
        }
    }

    fun getSelected(): ProductSize? {
        return flexibleAdapter.getSelectedItems().firstOrNull()?.data
    }
}