package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.data.domain.ModifierOption
import ji.shop.data.domain.ProductModifier
import ji.shop.data.domain.WrapperOptionModifier
import ji.shop.items.CountChangOnItemListener
import ji.shop.items.SelectionModifierOptionItemUi

class SelectionModifierOptionItemsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val flexibleAdapter = FlexibleAdapter<SelectionModifierOptionItemUi>(mutableListOf())
    private var productModifier: ProductModifier? = null

    init {
        isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = flexibleAdapter
    }

    fun setListener(onItemClickListener: CountChangOnItemListener) {
        flexibleAdapter.addListener(onItemClickListener)
    }

    fun setData(
        modifier: ProductModifier,
        items: List<ModifierOption>,
        selectedData: WrapperOptionModifier?
    ) {
        this.productModifier = modifier
        flexibleAdapter.updateDataset(items.map { option ->
            val count = selectedData?.items?.find { it.first.id == option.id }?.second ?: 0
            SelectionModifierOptionItemUi(option, count)
        })
    }

    fun getSelectedOptions(): Pair<ProductModifier, WrapperOptionModifier>? {
        return productModifier?.let { productModifier ->
            productModifier to WrapperOptionModifier(
                items = flexibleAdapter.items
                    .filter { it.count > 0 }
                    .map {
                        it.data to it.count
                    }
            )
        }
    }
}