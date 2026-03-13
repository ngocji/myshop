package ji.shop.items

import android.view.ViewGroup
import ji.shop.R
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.base.adapter.Payload
import ji.shop.data.domain.Cart
import ji.shop.databinding.ItemCartBinding
import ji.shop.exts.layoutInflate
import ji.shop.exts.load
import ji.shop.utils.NumberFormater

class CartItemUi(
    val data: Cart,
    var count: Int = data.count
) : ItemUI<ItemCartBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemCartBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        ).apply {
            withBinding(this) {
                toggleCountView.setListener { newCount ->
                    val item = adapter.getItem(absoluteAdapterPosition) as? CartItemUi
                        ?: return@setListener
                    item.count = newCount
                    adapter.notifyItemChanged(absoluteAdapterPosition, Payload.CHANGE_COUNT)
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
                        tvPrice.text = NumberFormater.formatNumberLocale(data.getTotalPrice(count))
                        return@withBinding
                    }
                }
            }

            tvName.text = data.product.name
            tvSize.text = data.size?.name.orEmpty()
            image.load(data.product.images.firstOrNull(), error = R.drawable.ic_product)
            toggleCountView.setCount(count)
            tvPrice.text = NumberFormater.formatNumberLocale(data.getTotalPrice(count))
        }
    }
}