package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import ji.shop.R
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.data.domain.ProductModifier
import ji.shop.data.domain.WrapperOptionModifier
import ji.shop.databinding.ItemProductModifierBinding
import ji.shop.items.CountChangOnItemListener

class GroupModifiersView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val layoutInflater by lazy { LayoutInflater.from(context) }
    private var onCountChangedListener: CountChangOnItemListener? = null

    fun setData(
        items: List<ProductModifier>,
        selectedData: Map<ProductModifier, WrapperOptionModifier>
    ) {
        removeAllViews()
        items.forEach { item ->
            val binding = ItemProductModifierBinding.inflate(layoutInflater)
            binding.tvAddTitle.text = item.name
            binding.selectionAdditionalItemsView.setData(
                modifier = item,
                items = item.options,
                selectedData = selectedData[item]
            )
            binding.selectionAdditionalItemsView.setListener(object : CountChangOnItemListener {
                override fun onCountChanged(position: Int, count: Int) {
                    onCountChangedListener?.onCountChanged(position, count)
                }

                override fun onClick(
                    adapter: FlexibleAdapter<*>,
                    view: View,
                    position: Int
                ) {
                    onCountChangedListener?.onClick(adapter, view, position)
                }
            })
            addView(binding.root, LayoutParams(-1,-2).apply {
                setMargins(0,0,0,context.resources.getDimensionPixelOffset(R.dimen.large_2_padding))
            })
        }
    }

    fun getSelectedData(): Map<ProductModifier, WrapperOptionModifier> {
        val map = mutableMapOf<ProductModifier, WrapperOptionModifier>()

        for (i in 0 until childCount) {
            val child = getChildAt(i).let { ItemProductModifierBinding.bind(it) }
            val pair = child.selectionAdditionalItemsView.getSelectedOptions()
            if (pair != null) {
                map[pair.first] = pair.second
            }
        }
        return map
    }

    fun setListener(onCountChangedListener: CountChangOnItemListener) {
        this.onCountChangedListener = onCountChangedListener
    }
}