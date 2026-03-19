package ji.shop.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.databinding.LayoutSelectionDropdownPopupBinding
import ji.shop.exts.height
import ji.shop.items.SelectionDropdownUi

class SelectionDropdownPopup<T : Any>(
    context: Context,
    private val items: List<T>,
    private val onSelectedItem: (T) -> Unit
) : PopupWindow(context) {
    private val binding: LayoutSelectionDropdownPopupBinding

    init {
        binding = LayoutSelectionDropdownPopupBinding.inflate(LayoutInflater.from(context))
        contentView = binding.root
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initAdapter()
    }

    fun show(targetView: View) {
        val targetWidth = targetView.measuredWidth
        binding.constraint.layoutParams =
            FrameLayout.LayoutParams(targetWidth, targetView.context.height() / 2)
        showAsDropDown(targetView, 0, 10)
    }

    private fun initAdapter() {
        val flexibleAdapter = FlexibleAdapter(items.map { SelectionDropdownUi(it) }.toMutableList())
            .addListener { adapter, view, i ->
                val item =
                    (adapter.getItem(i) as? SelectionDropdownUi)?.data as? T ?: return@addListener
                onSelectedItem(item)
                dismiss()
            }
        binding.recyclerView.adapter = flexibleAdapter
    }
}