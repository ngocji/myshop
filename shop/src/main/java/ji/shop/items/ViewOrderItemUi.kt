package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.domain.OrderItemDetail
import ji.shop.databinding.ItemViewOrderBinding
import ji.shop.exts.layoutInflate
import ji.shop.exts.load
import ji.shop.utils.NumberFormater

data class ViewOrderItemUi(
    val item: OrderItemDetail
) : ItemUI<ItemViewOrderBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemViewOrderBinding.inflate(
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
            imgPicture.load(item.imageUrl)
            tvName.text = item.orderItemName
            tvPrice.text = NumberFormater.formatNumberLocale(item.unitPrice * item.quantity)
            tvQuantity.text = "${item.quantity}"
            tvVariationName.text = buildString {
                append(item.variationName)
                if (item.variationName.isNotEmpty() && item.modifierName.isNotEmpty()) {
                    append(",")
                }
                append(item.modifierName)
            }
        }
    }
}