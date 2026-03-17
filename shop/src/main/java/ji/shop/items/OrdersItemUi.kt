package ji.shop.items

import android.view.ViewGroup
import androidx.core.view.isVisible
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.domain.Order
import ji.shop.databinding.ItemOrdersBinding
import ji.shop.exts.layoutInflate

data class OrdersItemUi(
    val order: Order
) : ItemUI<ItemOrdersBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemOrdersBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        ).apply {
            withBinding(this) {
                var expanded = info?.isVisible ?: false
                imgArrowDown?.setOnClickListener {
                    expanded = !expanded
                    imgArrowDown.animate()
                        .rotation(if (expanded) 180f else 0f)
                        .setDuration(200)
                        .start()
                    info?.isVisible = expanded
                }

                imgAction.setOnClickListener {
                    adapter.notifyListeners { onClick(adapter, imgAction, absoluteAdapterPosition) }
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
            //rcvAvatar?.setData(cart.product.images)
            tvProductName.text = order.name
            tvQty.text = order.quantity.toString()
            tvTime.text = order.time
            tvOrderMethod?.text = order.paymentMethod
            tvStatus.setState(order.status)
            //tvNote?.text = cart.product.description
        }
    }
}