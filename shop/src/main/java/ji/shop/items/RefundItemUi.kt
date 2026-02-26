package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.Cart
import ji.shop.data.Group
import ji.shop.databinding.ItemGroupBinding
import ji.shop.databinding.ItemRefundBinding
import ji.shop.exts.layoutInflate
import ji.shop.utils.NumberFormater

class RefundItemUi(val data: Cart) : ItemUI<ItemRefundBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemRefundBinding.inflate(
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
            tvQty.text = data.count.toString()
            tvName.text = data.product.name
            tvPrice.text = NumberFormater.formatNumberLocale(data.product.price)
        }
    }
}