package ji.shop.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.viewBinding
import ji.shop.databinding.DialogViewCardInfoBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import kotlin.math.roundToInt

class ViewCardInfoDialog : BaseDialog(R.layout.dialog_view_card_info) {
    private val binding by viewBinding(DialogViewCardInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    override fun doOnWindow(window: Window) {
        super.doOnWindow(window)
        val isTablet = context.isTablet()
        window.setLayout(
            requireActivity().width().let {
                if (isTablet) (it * 0.4).roundToInt() else it
            },
            requireActivity().height().let {
                if (isTablet) it else (it * 0.7).roundToInt()
            }
        )
        window.setGravity(if (isTablet) Gravity.END else Gravity.BOTTOM)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    private fun initViews() {
        with(binding) {
            btnBack.setOnClickListener { dismissAllowingStateLoss() }
            btnPlaceOrder.setOnClickListener {

            }
        }
    }

    private fun initData() {

    }

    companion object {
        fun newInstance(): ViewCardInfoDialog {
            return ViewCardInfoDialog().apply {

            }
        }
    }
}