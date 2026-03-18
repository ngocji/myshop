package ji.shop.items

import android.view.ViewGroup
import androidx.core.view.isVisible
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.base.adapter.Payload
import ji.shop.data.domain.ModifierOption
import ji.shop.databinding.ItemSelectionModifierOptionBinding
import ji.shop.exts.layoutInflate
import ji.shop.utils.NumberFormater

data class SelectionModifierOptionItemUi(
    val data: ModifierOption,
    var count: Int
) : ItemUI<ItemSelectionModifierOptionBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemSelectionModifierOptionBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        ).apply {
            withBinding(this) {
                toggleCountView.setListener { newCount ->
                    val item =
                        adapter.getItem(absoluteAdapterPosition) as? SelectionModifierOptionItemUi
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