package ji.shop.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import ji.shop.R
import ji.shop.databinding.LayoutPopupMenuBinding

class PopupWindow(
    private val context: Context,
    private val anchor: View,
) {
    fun show() {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_popup_menu, null)

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(anchor)

        val binding = LayoutPopupMenuBinding.bind(view)

        binding.btnVewOrder.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.btnRefund.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.btnCouponsReport.setOnClickListener {
            popupWindow.dismiss()
        }
    }
}