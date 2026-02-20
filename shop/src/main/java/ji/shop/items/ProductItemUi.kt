package ji.shop.items

import android.view.ViewGroup
import androidx.core.view.isVisible
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.base.adapter.Payload
import ji.shop.data.Product
import ji.shop.databinding.ItemProductBinding
import ji.shop.exts.layoutInflate
import ji.shop.exts.load
import ji.shop.utils.NumberFormater

data class ProductItemUi(
    val data: Product,
    var count: Int,
    var isUseToggleCount: Boolean
) : ItemUI<ItemProductBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemProductBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        ).apply {
            withBinding(this) {
                toggleCountView.setListener { newCount ->
                    count = newCount
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
                        toggleCountView.setCount(count,skipZero = false)
                        return@withBinding
                    }
                    if (obj == Payload.CHANGE_USE_TOGGLE_COUNT) {
                        toggleCountView.isVisible = isUseToggleCount
                        return@withBinding
                    }
                }
            }

            tvName.text = data.name
            tvPrice.text = NumberFormater.formatNumberLocale(data.price)
            image.load(data.images.firstOrNull())
            toggleCountView.setCount(count,skipZero = false)
            toggleCountView.isVisible = isUseToggleCount
        }
    }
}