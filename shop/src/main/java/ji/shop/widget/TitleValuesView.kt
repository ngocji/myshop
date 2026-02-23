package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.Payload
import ji.shop.items.TitleValueItemUi

class TitleValuesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private var isBoldValue = true
    private val flexibleAdapter: FlexibleAdapter<TitleValueItemUi> =
        FlexibleAdapter(mutableListOf())

    init {
        setHasFixedSize(true)
        isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = flexibleAdapter
    }

    fun setBoldValue(isBoldValue: Boolean) {
        this.isBoldValue = isBoldValue
    }

    fun setData(vararg values: Pair<Int, Any>) {
        if (values.size == flexibleAdapter.itemCount) {
            flexibleAdapter.items.forEachIndexed { index, item ->
                item.data = values[index]
            }
            flexibleAdapter.notifyItemRangeChanged(0, flexibleAdapter.itemCount, Payload.CHANGE)
            return
        }
        val items = values.map { TitleValueItemUi(it, isBoldValue = isBoldValue) }
        flexibleAdapter.updateDataset(items)
    }
}