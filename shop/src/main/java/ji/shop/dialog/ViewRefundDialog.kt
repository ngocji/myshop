package ji.shop.dialog

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.viewBinding
import ji.shop.databinding.DialogViewRefundBinding

class ViewRefundDialog : BaseDialog(R.layout.dialog_view_refund) {

    private val binding by viewBinding(DialogViewRefundBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initViews() {

    }

    private fun initData() {

    }

    companion object {
        fun newInstance(): ViewRefundDialog {
            return ViewRefundDialog()
        }
    }
}