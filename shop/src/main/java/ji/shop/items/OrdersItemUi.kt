package ji.shop.items

import android.view.ViewGroup
import androidx.core.view.isVisible
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.Cart
import ji.shop.databinding.ItemOrdersBinding
import ji.shop.exts.layoutInflate
import ji.shop.utils.DateFormater
import ji.shop.widget.PopupAction
import ji.shop.widget.PopupWindow

data class OrdersItemUi(
    val cart: Cart
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
            rcvAvatar?.setData(cart.product.images)
            tvProductName.text = cart.product.name
            tvQty.text = cart.count.toString()
            tvTime.text = DateFormater.format(cart.date, "hh:mm a")
            tvOrderMethod?.text = cart.count.toString()
            tvStatus.setState(cart.product.status)
            tvNote?.text = cart.product.description
        }
    }
}