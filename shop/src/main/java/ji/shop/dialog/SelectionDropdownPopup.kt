package ji.shop.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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

    fun show(
        targetView: View,
        targetWidth: Int = targetView.measuredWidth,
        targetHeight: Int = targetView.context.height() / 2,
        xOff: Int = 0,
        yOff: Int = 10,
        gravity: Int = Gravity.TOP or Gravity.START
    ) {
        binding.constraint.layoutParams =
            FrameLayout.LayoutParams(targetWidth, targetHeight)
        showAsDropDown(targetView, xOff, yOff, gravity)
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