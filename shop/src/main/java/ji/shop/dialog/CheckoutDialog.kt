package ji.shop.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.viewBinding
import ji.shop.data.Cart
import ji.shop.data.Repo
import ji.shop.databinding.DialogViewCheckoutBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.utils.DateFormater
import ji.shop.utils.NumberFormater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class CheckoutDialog : BaseDialog(R.layout.dialog_view_checkout) {
    private val binding by viewBinding(DialogViewCheckoutBinding::bind)
    private var items: List<Cart>? = null

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
                if (isTablet) (it * 0.4).roundToInt() else it
            },
            requireActivity().height().let {
                if (isTablet) it else (it * 0.7).roundToInt()
            }
        )
        window.setGravity(if (isTablet) Gravity.END else Gravity.BOTTOM)
    }

    private fun initViews() {
        with(binding) {
            btnCash.setOnClickListener {

            }
            btnCredit.setOnClickListener { }
            btnAddCustomerInfo.setOnClickListener { }
            btnTickets.setOnClickListener { toggleTicketView() }
            btnDone.setOnClickListener { dismissAllowingStateLoss() }
        }
    }

    private fun toggleTicketView() {
        val newVisible = !binding.ticketView.isVisible
        binding.ticketView.isVisible = newVisible
        binding.btnTickets.setIconResource(
            if (newVisible) R.drawable.ic_circle_arrow_up else R.drawable.ic_circle_arrow_down
        )
    }

    private fun initData() {
        with(binding) {
            val itemPrice = items?.sumOf { it.getTotalPrice() } ?: 0.0
            val tax = itemPrice * 0.038f
            tvTotal.text = NumberFormater.formatNumberLocale(itemPrice + tax)
        }
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
                    tvFestival.text = "${items?.size ?: 0}x${ticket.ticketDayPass}}"
                    tvFestivalValue.text = "${items?.size ?: 0}x${tvTotal.text}"
                    titleValuesView.setBoldValue(false)
                    titleValuesView.setData(*info.toTypedArray())
                }
            }
        }
    }

    companion object {
        fun newInstance(items: List<Cart>): CheckoutDialog {
            return CheckoutDialog().apply {
                this.items = items
            }
        }
    }
}