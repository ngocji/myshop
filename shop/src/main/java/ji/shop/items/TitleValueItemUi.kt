package ji.shop.items

import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import ji.shop.R
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.adapter.ItemViewHolder
import ji.shop.databinding.ItemTitleValueBinding
import ji.shop.exts.layoutInflate

class TitleValueItemUi(
    val data: Pair<Int, Any>,
    private val isBoldValue: Boolean = true
) : ItemUI<ItemTitleValueBinding>() {
    override fun createViewHolder(
        adapter: FlexibleAdapter<*>,
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemTitleValueBinding.inflate(
                parent.layoutInflate(),
                parent,
                false
            ).apply {
                if (!isBoldValue) {
                    TextViewCompat.setTextAppearance(tvValue, R.style.fontMedium)
                }
            }
        )
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<*>,
        holder: ItemViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        withBinding(holder) {
            tvTitle.setText(data.first.takeIf { it > 0 } ?: R.string.text_empty)
            tvValue.text = data.second.toString()
        }
    }
}