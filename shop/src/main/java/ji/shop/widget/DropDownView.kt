package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import ji.shop.R
import ji.shop.databinding.DropDownViewBinding
import ji.shop.dialog.SelectionDropdownPopup

class DropDownView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: DropDownViewBinding
    private var items: List<Any>? = null
    private var selectItem: Any? = null
    private var onSelected: ((Any) -> Unit)? = null

    init {
        binding = DropDownViewBinding.inflate(LayoutInflater.from(context), this)
        setBackgroundResource(R.drawable.bg_secondary_rounded_with_stroke)
        gravity = Gravity.CENTER
        setOnClickListener { doShowSelection() }
    }

    fun <T : Any> setData(items: List<T>?, selectItem: T?, onSelected: (T) -> Unit) {
        this.items = items
        this.selectItem = selectItem
        this.onSelected = onSelected as? ((Any) -> Unit)?

        setSelect(selectItem)
    }

    fun <T> setSelect(item: T?) {
        binding.spinner.text = item?.toString() ?: ""
    }

    private fun doShowSelection() {
        SelectionDropdownPopup(context, items ?: emptyList(), onSelected ?: {}).show(this)
    }
}