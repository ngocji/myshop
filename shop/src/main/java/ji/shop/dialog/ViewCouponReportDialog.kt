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
import ji.shop.databinding.DialogCouponsReportBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.ViewOrderItemUi
import ji.shop.utils.NumberFormater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ViewCouponReportDialog : BaseDialog(R.layout.dialog_coupons_report) {
    private val binding by viewBinding(DialogCouponsReportBinding::bind)
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
        }
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val viewOrder = Repo.getCouponsReport(postOrderId)

            withContext(Dispatchers.Main) {
                val data = viewOrder?.items?.map { ViewOrderItemUi(it) } ?: emptyList()
                flexibleAdapter = FlexibleAdapter(data.toMutableList())
                binding.recyclerView.adapter = flexibleAdapter

                with(binding) {
                    viewOrder?.orderInfo?.apply {
                        tvName.text = buyerName
                        tvPhone.text = buyerPhone
                        tvMail.text = buyerEmail
                        tvTime.text = time
                        tvPaymentMethod.text = paymentMethod
                        tvPaid.text = "Paid"
                        tvTitle.text = String.format(getString(R.string.text_order), posOrderId)

                        viewOrder.summary?.apply {
                            tvSubTotal.setValue(NumberFormater.formatNumberLocale(subtotal))
                            tvTax.setValue(NumberFormater.formatNumberLocale(tax))
                            tvTotal.setValue(NumberFormater.formatNumberLocale(total))
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(postOrderId: String?): ViewCouponReportDialog {
            return ViewCouponReportDialog().apply {
                this.postOrderId = postOrderId
            }
        }
    }
}