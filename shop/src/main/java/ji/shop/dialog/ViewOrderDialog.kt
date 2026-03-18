package ji.shop.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.lifecycle.lifecycleScope
import ji.shop.R
import ji.shop.ShopSDK
import ji.shop.base.BaseDialog
import ji.shop.base.adapter.FlexibleAdapter
import ji.shop.base.adapter.ItemUI
import ji.shop.base.viewBinding
import ji.shop.data.Repo
import ji.shop.data.dto.RequestRefund
import ji.shop.data.dto.toRequest
import ji.shop.databinding.DialogViewOrderBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import ji.shop.items.RefundItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ViewOrderDialog : BaseDialog(R.layout.dialog_view_order) {
    private val binding by viewBinding(DialogViewOrderBinding::bind)
    private var postOrderId: String? = null
    private var requestRefund: RequestRefund? = null
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

        }
    }

    companion object {
        fun newInstance(postOrderId: String?): ViewOrderDialog {
            return ViewOrderDialog().apply {
                this.postOrderId = postOrderId
            }
        }
    }
}