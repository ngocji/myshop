package ji.shop.items

import android.view.ViewGroup
import androidx.core.view.isVisible
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.base.adapter.Payload
import ji.shop.data.domain.Product
import ji.shop.databinding.ItemProductBinding
import ji.shop.exts.layoutInflate
import ji.shop.exts.load
import ji.shop.utils.NumberFormater

data class ProductItemUi(
    val data: Product,
    var count: Int = 0,
    private val isSignSelectionProduct: Boolean = data.isSingleSelection()
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
                toggleCountView.setHideWhenCountingZero(false)
                toggleCountView.setListener { newCount ->
                    val item = adapter.getItem(absoluteAdapterPosition) as? ProductItemUi
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
                    if (obj == Payload.CHANGE_COUNT || obj == Payload.CHANGE_USE_TOGGLE_COUNT) {
                        toggleCountView.setCount(count)
                        return@withBinding
                    }
                }
            }

            tvName.text = data.name
            tvPrice.text = NumberFormater.formatNumberLocale(data.price)
            image.load(data.images.firstOrNull())
            if (data.isSoldOut) {
                toggleCountView.isVisible = false
                tvSold.isVisible = true
            } else {
                if (isSignSelectionProduct) {
                    toggleCountView.isVisible = true
                    toggleCountView.setCount(count)
                } else {
                    toggleCountView.isVisible = false
                }
            }
        }
    }
}