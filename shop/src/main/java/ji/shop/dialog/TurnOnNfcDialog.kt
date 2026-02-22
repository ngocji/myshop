package ji.shop.dialog

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.viewBinding
import ji.shop.databinding.DialogTurnOnNfcBinding

class TurnOnNfcDialog : BaseDialog(R.layout.dialog_turn_on_nfc) {
    private val binding by viewBinding(DialogTurnOnNfcBinding::bind)
    private var onEnabled: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            btnCancel.setOnClickListener { dismissAllowingStateLoss() }
            btnConfirm.setOnClickListener {
                onEnabled?.invoke()
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        fun newInstance(onEnabled: () -> Unit): TurnOnNfcDialog {
            val dialog = TurnOnNfcDialog()
            dialog.onEnabled = onEnabled
            return dialog
        }
    }
}