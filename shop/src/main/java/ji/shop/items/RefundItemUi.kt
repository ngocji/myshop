package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.domain.RefundItem
import ji.shop.databinding.ItemRefundBinding
import ji.shop.databinding.ItemRefundTotalBinding
import ji.shop.exts.layoutInflate
import ji.shop.utils.NumberFormater

class RefundItemUi(
    val data: RefundItem,
    var count: Int = 0
) : ItemUI<ItemRefundBinding>() {
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
            tvQty.text = data.quantity.toString()
            tvName.text = data.name
            tvPrice.text = NumberFormater.formatNumberLocale(data.unitPrice)
            tvValue.text = count.toString()
        }
    }


    companion object {
        const val PAYLOAD_CHANGE_COUNT = "payload_change_count"
    }
}

class TotalRefundItemUi(var totalPrice: Double) : ItemUI<ItemRefundTotalBinding>() {
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
            tvPriceTotal.text = NumberFormater.formatNumberLocale(totalPrice ?: 0.0)
        }
    }

    companion object {
        const val PAYLOAD_CHANGE_TOTAL = "payload_change_total"
    }
}
