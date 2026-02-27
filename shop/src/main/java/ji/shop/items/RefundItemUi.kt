package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.Cart
import ji.shop.databinding.ItemRefundBinding
import ji.shop.databinding.ItemRefundTotalBinding
import ji.shop.exts.layoutInflate
import ji.shop.utils.NumberFormater

class RefundItemUi {
    class RefundItem(val data: Cart) : ItemUI<ItemRefundBinding>() {
        override fun getItemViewType(): Int {
            return 0
        }
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

    class TotalRefundItem(val totalPrice: Double) : ItemUI<ItemRefundTotalBinding>() {
        override fun getItemViewType(): Int {
            return 1
        }
        override fun createViewHolder(
            adapter: FlexibleAdapter<*>,
            parent: ViewGroup,
            viewType: Int
        ): ItemViewHolder {
            return ItemViewHolder(
                ItemRefundTotalBinding.inflate(
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
                tvPriceTotal.text = NumberFormater.formatNumberLocale(totalPrice)
            }
        }
    }
}
