package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.base.adapter.Payload
import ji.shop.data.Cart
import ji.shop.databinding.ItemCartBinding
import ji.shop.exts.layoutInflate
import ji.shop.exts.load

class CartItemUi(val data: Cart) : ItemUI<ItemCartBinding>() {
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
                        toggleCountView.setCount(data.count)
                        return@withBinding
                    }
                }
            }
            tvName.text = data.product.name
            tvSize.text = data.size?.name.orEmpty()
            image.load(data.product.images.firstOrNull())
            toggleCountView.setCount(data.count)
        }
    }
}