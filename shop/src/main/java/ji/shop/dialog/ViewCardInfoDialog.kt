package ji.shop.dialog

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.lifecycle.lifecycleScope
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.FlexibleAdapter.Companion.SINGLE
import ji.shop.base.viewBinding
import ji.shop.data.CardMethod
import ji.shop.data.Checkout
import ji.shop.data.Repo
import ji.shop.databinding.DialogViewCardInfoBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.CardInfoItemUi
import ji.shop.utils.DateFormater
import ji.shop.utils.NumberFormater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ViewCardInfoDialog : BaseDialog(R.layout.dialog_view_card_info) {
    private val binding by viewBinding(DialogViewCardInfoBinding::bind)
    private var flexibleAdapter: FlexibleAdapter<CardInfoItemUi>? = null
    private var checkout: Checkout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
        initTicket()
    }

    override fun doOnWindow(window: Window) {
        super.doOnWindow(window)
        val isTablet = context.isTablet()
        window.setLayout(
            requireActivity().width().let {
                if (isTablet) (it * 0.7).roundToInt() else it
            },
            requireActivity().height().let {
                if (isTablet) (it * 0.7).roundToInt() else it
            }
        )
        window.setGravity(if (isTablet) Gravity.CENTER else Gravity.BOTTOM)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    private fun initViews() {

        with(binding) {
            tvCardNumber.text = checkout?.creditInfo?.cardNumber.orEmpty()

            btnPlaceOrder.text = String.format(
                resources.getString(R.string.text_place_order),
                NumberFormater.formatNumberLocale(getTotal())
            )

            btnBack.setOnClickListener { dismissAllowingStateLoss() }

            btnPlaceOrder.setOnClickListener {

            }
        }
    }

    private fun getTotal(): Double {
        val itemPrice = checkout?.items?.sumOf { it.getTotalPrice() } ?: 0.0
        val tax = itemPrice * 0.038f
        return itemPrice + tax
    }

    @SuppressLint("SetTextI18n")
    private fun initTicket() {
        viewLifecycleOwner.lifecycleScope.launch {
            val ticket = Repo.getTicket()
            val info = ticket.info.map { entry ->
                entry.key to NumberFormater.formatNumberLocale(entry.value)
            }
            withContext(Dispatchers.Main) {
                with(binding) {
                    tvName.text = ticket.name
                    tvTime.text = DateFormater.format(ticket.date, "EEE, MMM dd, yyyy, hh:mm a")
                    tvFestival.text = "${checkout?.items?.size ?: 0} x ${ticket.ticketDayPass}"
                    tvFestivalValue.text = "${checkout?.items?.size ?: 0} x ${NumberFormater.formatNumberLocale(getTotal())}"
                    titleValuesView.setBoldValue(false)
                    titleValuesView.setData(*info.toTypedArray())
                }
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