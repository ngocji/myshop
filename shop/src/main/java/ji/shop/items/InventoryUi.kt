package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.Inventory
import ji.shop.databinding.ItemInventoryBinding
import ji.shop.exts.layoutInflate
import ji.shop.exts.load
import ji.shop.utils.NumberFormater
import ji.shop.utils.NumberFormater.toPlanString

class InventoryUi(val inventory: Inventory) : ItemUI<ItemInventoryBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemInventoryBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        ).apply {
            withBinding(this) {
                imgArrowDown?.setOnClickListener {

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
            imgPicture.load(inventory.image)
            imgPicture.isSelected = inventory.isVisibility
            tvName.text = inventory.name
            tvOrders.text = inventory.orders.toPlanString()
            tvComplete.text = inventory.complete.toPlanString()
            tvPrice.text = NumberFormater.formatNumberLocale(inventory.price)
            tvSold.text = inventory.sold.toPlanString()
            prgRemaining.setProgress(inventory.remaining)
            tvQuantity.text = inventory.quantity.toPlanString()
        }
    }

}