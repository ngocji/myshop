package ji.shop.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.FlexibleAdapter.Companion.SINGLE
import ji.shop.base.viewBinding
import ji.shop.data.CardMethod
import ji.shop.data.Checkout
import ji.shop.databinding.DialogViewCardInfoBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CardInfoItemUi
import ji.shop.utils.NumberFormater
import kotlin.math.roundToInt

class ViewCardInfoDialog : BaseDialog(R.layout.dialog_view_card_info) {
    private val binding by viewBinding(DialogViewCardInfoBinding::bind)
    private var flexibleAdapter: FlexibleAdapter<CardInfoItemUi>? = null
    private var checkout: Checkout? = null

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
            tvCardNumber.text = checkout?.creditInfo?.cardNumber.orEmpty()
            tvCouponDiscount.text = NumberFormater.formatNumberLocale(0.0)
            tvFaceValue.text = NumberFormater.formatNumberLocale(1.0)
            tvDonation.text = NumberFormater.formatNumberLocale(1.0)
            tvServiceFee.text = NumberFormater.formatNumberLocale(1.0)
            tvSubtotal.text = NumberFormater.formatNumberLocale(1.0)
            tvTaxes.text = NumberFormater.formatNumberLocale(1.0)
            tvTotal.text = NumberFormater.formatNumberLocale(6.0)
            btnPlaceOrder.text = String.format(
                resources.getString(R.string.text_place_order),
                NumberFormater.formatNumberLocale(25.44)
            )

            btnBack.setOnClickListener { dismissAllowingStateLoss() }

            btnPlaceOrder.setOnClickListener {

            }
        }
    }

    private fun initData() {
        val data = CardMethod.all().map { CardInfoItemUi(it) }
        flexibleAdapter = FlexibleAdapter(data.toMutableList())
            .setMode(SINGLE)
            .addListener { adapter, _, position ->
                if (!adapter.isSelected(position)) {
                    adapter.toggleSelection(position)
                }
            }
        if (flexibleAdapter?.isSelected(0) == false) {
            flexibleAdapter?.toggleSelection(0)
        }
        binding.rcvCardMethod.adapter = flexibleAdapter
    }

    companion object {
        fun newInstance(checkout: Checkout?): ViewCardInfoDialog {
            return ViewCardInfoDialog().apply {
                this.checkout = checkout
            }
        }
    }
}