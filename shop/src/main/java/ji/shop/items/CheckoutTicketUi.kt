package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.domain.CardMethod
import ji.shop.data.domain.Cart
import ji.shop.data.domain.ShopCategory
import ji.shop.databinding.ItemCheckoutTicketBinding
import ji.shop.exts.layoutInflate

class CheckoutTicketUi(
    val shopCategory: ShopCategory,
    val carts: List<Cart>,
    val cardMethod: CardMethod,
) : ItemUI<ItemCheckoutTicketBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemCheckoutTicketBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            )
        ).apply {
            withBinding(this) {

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
            tvName.text = shopCategory.name
            checkoutProductItemsView.setData(carts, cardMethod)
        }
    }
}