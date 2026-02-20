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
    private var flexibleAdapter: FlexibleAdapter<SelectTabViewItemUi<*>>? = null

    init {
        setBackgroundResource(R.drawable.bg_secondary_rounded_with_stroke)
        context.resources.getDimensionPixelOffset(R.dimen._6dp).also {
            setPadding(it, it, it, it)
        }
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun <T> setData(
        items: List<T>,
        selectedIndex: Int = 0,
        onGetTitle: (T) -> String,
        onItemSelected: (T) -> Unit
    ) {
        flexibleAdapter =
            FlexibleAdapter<SelectTabViewItemUi<*>>(items.map { SelectTabViewItemUi(it, onGetTitle(it)) }
                .toMutableList())
                .setMode(SINGLE)
                .addListener { adapter, _, position ->
                    if (!adapter.isSelected(position)) {
                        adapter.toggleSelection(position)
                        onItemSelected(items[position])
                    }
                }
        flexibleAdapter?.addAdjustSelected(selectedIndex)
        adapter = flexibleAdapter
    }

    fun <T> setSelected(item: T) {
        flexibleAdapter?.run {
            val index = items.indexOfFirst { it.data == item }
            if (index >= 0 && !isSelected(index)) {
                toggleSelection(index)
            }
        }
    }
}