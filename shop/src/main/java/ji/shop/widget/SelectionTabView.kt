package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.R
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.FlexibleAdapter.Companion.SINGLE
import ji.shop.items.SelectTabViewItemUi

class SelectionTabView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    init {
        setBackgroundResource(R.drawable.bg_secondary_rounded_with_stroke)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun <T> setData(
        items: List<T>,
        selectedIndex: Int = 0,
        onItemSelected: (T) -> Unit
    ) {
        val flexibleAdapter = FlexibleAdapter(items.map { SelectTabViewItemUi(it) }.toMutableList())
            .setMode(SINGLE)
            .addListener { adapter, _, position ->
                if (!adapter.isSelected(position)) {
                    onItemSelected(items[position])
                    adapter.toggleSelection(position)
                }
            }
        flexibleAdapter.addAdjustSelected(selectedIndex)
        adapter = flexibleAdapter
    }
}