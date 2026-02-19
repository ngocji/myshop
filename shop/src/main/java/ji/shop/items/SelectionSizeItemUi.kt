package ji.shop.items

import android.view.ViewGroup
import androidx.core.view.isVisible
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.ProductSize
import ji.shop.databinding.ItemSelectionSizeBinding
import ji.shop.exts.layoutInflate
import ji.shop.utils.NumberFormater

class SelectionSizeItemUi(val data: ProductSize) : ItemUI<ItemSelectionSizeBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemSelectionSizeBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<*>,
        holder: ItemViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        withBinding(holder) {
            tvName.text = data.name
            tvPrice.text = NumberFormater.formatNumberLocale(data.price)
            line.isVisible = position < adapter.itemCount - 1
            root.isSelected = adapter.isSelected(position)
        }
    }
}