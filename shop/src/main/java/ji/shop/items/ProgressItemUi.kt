package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.databinding.ItemProgressBinding
import ji.shop.exts.layoutInflate

object ProgressItemUi : ItemUI<ItemProgressBinding>() {
    override fun getItemViewType(): Int {
        return 999
    }

    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemProgressBinding.inflate(
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
    }
}