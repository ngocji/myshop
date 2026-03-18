package ji.shop.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.lifecycle.lifecycleScope
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.viewBinding
import ji.shop.data.Repo
import ji.shop.databinding.DialogViewRefundBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.RefundItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ViewRefundDialog : BaseDialog(R.layout.dialog_view_refund) {
    private val binding by viewBinding(DialogViewRefundBinding::bind)
    private var postOrderId: String? = null
    private var flexibleAdapter: FlexibleAdapter<ItemUI<*>>? = null

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
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnRefund.setOnClickListener {
                //ViewCardInfoDialog.newInstance(checkout).show(childFragmentManager)
            }
        }
    }

    private fun initData() {

        viewLifecycleOwner.lifecycleScope.launch {
            val refund = Repo.getRefund(postOrderId)

            withContext(Dispatchers.Main) {
                val data = (refund?.items.orEmpty()
                    .map { RefundItemUi.RefundItem(it) } +
                        RefundItemUi.TotalRefundItem(refund?.summary?.refundableAmount))
                    .toMutableList()

                flexibleAdapter = FlexibleAdapter(data)
                binding.recyclerView.adapter = flexibleAdapter

                refund?.customerInfo?.apply {
                    binding.tvName.text = name
                    binding.tvPhone.text = phoneNumber
                    binding.tvMail.text = email
                }
            }
        }
    }

    companion object {
        fun newInstance(postOrderId: String): ViewRefundDialog {
            return ViewRefundDialog().apply {
                this.postOrderId = postOrderId
            }
        }
    }
}