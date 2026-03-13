package ji.shop.items

import android.view.ViewGroup
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.data.domain.Collection
import ji.shop.databinding.ItemCollectionGridBinding
import ji.shop.exts.layoutInflate
import ji.shop.exts.load

class CollectionGridItemUi(val data: Collection) : ItemUI<ItemCollectionGridBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemCollectionGridBinding.inflate(
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
            tv.text = data.name
            image.load(data.image.firstOrNull())
            root.isSelected = adapter.isSelected(position)
        }
    }
}