package ji.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import ji.shop.R
import ji.shop.ShopViewModel
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.viewBinding
import ji.shop.data.domain.CardMethod
import ji.shop.data.domain.Cart
import ji.shop.data.domain.CustomerInfo
import ji.shop.data.domain.ResultWrapper
import ji.shop.databinding.DialogViewCheckoutBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CheckoutTicketUi
import ji.shop.utils.NumberFormater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CheckoutDialog : BaseDialog(R.layout.dialog_view_checkout) {
    private val binding by viewBinding(DialogViewCheckoutBinding::bind)
    private var items: List<Cart>? = null
    private var listener: Listener? = null
    private var usedCardMethod: CardMethod = CardMethod.Cash
    private var flexibleCheckoutTickets: FlexibleAdapter<CheckoutTicketUi>? = null

    private val viewModel by activityViewModels<ShopViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        toggleCardMethod(usedCardMethod, true)
        reloadFees()
    }

    override fun doOnWindow(window: Window) {
        super.doOnWindow(window)
        val isTablet = context.isTablet()
        window.setLayout(
            requireActivity().width().let {
                if (isTablet) (it * 0.4).roundToInt() else it
            },
            requireActivity().height().let {
                if (isTablet) it else (it * 0.9).roundToInt()
            }
        )
        window.setGravity(if (isTablet) Gravity.END else Gravity.BOTTOM)
    }

    private fun initViews() {
        with(binding) {
            btnCash.setOnClickListener {
                toggleCardMethod(CardMethod.Cash)
                reloadFees()
            }
            btnCredit.setOnClickListener {
                toggleCardMethod(CardMethod.Credit)
                reloadFees()
            }
            btnAddCustomerInfo.setOnClickListener { }
            btnTickets.setOnClickListener { toggleTicketView() }
            btnDone.setOnClickListener {
                listener?.onDone(usedCardMethod)
                dismissAllowingStateLoss()
            }
        }
    }

    private fun toggleCardMethod(cardMethod: CardMethod, force: Boolean = false) {
        if (usedCardMethod == cardMethod && !force) return
        usedCardMethod = cardMethod
        with(binding) {
            btnCash.alpha = if (usedCardMethod == CardMethod.Cash) 1f else 0.5f
            btnCredit.alpha = if (usedCardMethod == CardMethod.Credit) 1f else 0.5f
        }
    }

    private fun toggleTicketView() {
        val newVisible = !binding.ticketView.isVisible
        binding.ticketView.isVisible = newVisible
        binding.btnTickets.setIconResource(
            if (newVisible) R.drawable.ic_circle_arrow_up else R.drawable.ic_circle_arrow_down
        )
    }

    private fun reloadFees() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getShoppingFees(items, usedCardMethod)
                .collect { resultWrapper ->
                    binding.stateView.updateState(resultWrapper)
                    if (resultWrapper is ResultWrapper.Success) {
                        val fees = resultWrapper.safeValue() ?: return@collect
                        val totalPrice = NumberFormater.formatNumberLocale(fees.totalMoney)
                        val feesTitleValue = listOf(
                            getString(R.string.text_coupon_discount) to NumberFormater.formatNumberLocale(
                                fees.couponDiscount
                            ),
                            getString(R.string.text_face_value) to NumberFormater.formatNumberLocale(
                                fees.basePrice
                            ),
                            getString(R.string.text_donation) to NumberFormater.formatNumberLocale(
                                0.0
                            ),
                            getString(R.string.text_service_fee) to NumberFormater.formatNumberLocale(
                                fees.estFee
                            ),
                            getString(R.string.text_subtotal) to NumberFormater.formatNumberLocale(
                                fees.subTotal
                            ),
                            getString(R.string.text_taxes) to NumberFormater.formatNumberLocale(fees.estSalesTax),
                            getString(R.string.text_total) to totalPrice
                        )

                        val checkoutItems = items?.groupBy { it.shop }
                            ?.mapNotNull { entry ->
                                val shop = entry.key
                                if (shop != null) {
                                    CheckoutTicketUi(
                                        shopCategory = shop,
                                        carts = entry.value,
                                        cardMethod = usedCardMethod
                                    )
                                } else {
                                    null
                                }
                            }

                        with(binding) {
                            ticketView.isVisible = true
                            tvTotal.text = totalPrice
                            titleValuesView.setBoldValue(false)
                            titleValuesView.setData(*feesTitleValue.toTypedArray())
                            updateCheckoutTickets(checkoutItems ?: emptyList())
                        }
                    }
                }
        }
    }

    private fun updateCheckoutTickets(items: List<CheckoutTicketUi>) {
        flexibleCheckoutTickets?.updateDataset(items) ?: run {
            flexibleCheckoutTickets = FlexibleAdapter(items.toMutableList())
        }
        binding.recyclerViewTickets.adapter = flexibleCheckoutTickets
    }

    interface Listener {
        fun onUpdateCustomerInfo(customerInfo: CustomerInfo?)
        fun onDone(method: CardMethod)
    }

    companion object {
        fun newInstance(
            items: List<Cart>,
            cardMethod: CardMethod,
            listener: Listener
        ): CheckoutDialog {
            return CheckoutDialog().apply {
                this.items = items
                this.usedCardMethod = cardMethod
                this.listener = listener
            }
        }
    }
}