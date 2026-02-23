package ji.shop.items

import android.view.ViewGroup
import androidx.core.view.isVisible
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.base.adapter.Payload
import ji.shop.data.ProductAdditional
import ji.shop.databinding.ItemSelectionAdditionalBinding
import ji.shop.exts.layoutInflate
import ji.shop.utils.NumberFormater

data class SelectionAdditionalItemUi(
    val data: ProductAdditional,
    var count: Int
) : ItemUI<ItemSelectionAdditionalBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemSelectionAdditionalBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        ).apply {
            withBinding(this) {
                toggleCountView.setListener { newCount ->
                    val item = adapter.getItem(absoluteAdapterPosition) as? SelectionAdditionalItemUi
                    item?.count = newCount
                    adapter.notifyListeners {
                        if (this is CountChangOnItemListener) {
                            onCountChanged(absoluteAdapterPosition, newCount)
                        }
                    }
                }
            }
        }
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<*>,
        holder: ItemViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        withBinding(holder) {
            if (payloads.isNotEmpty()) {
                payloads.forEach { obj ->
                    if (obj == Payload.CHANGE_COUNT) {
                        toggleCountView.setCount(count)
                        return@withBinding
                    }
                }
            }

            tvName.text = data.name
            tvPrice.text = NumberFormater.formatNumberLocale(data.price)
            toggleCountView.setCount(count)
            line.isVisible = position < adapter.itemCount - 1
            root.isSelected = adapter.isSelected(position)
        }
    }
}