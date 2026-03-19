package ji.shop.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ji.shop.data.domain.CardMethod
import ji.shop.data.domain.Cart
import ji.shop.databinding.ItemCheckoutProductViewBinding
import ji.shop.utils.NumberFormater

class CheckoutProductItemsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val inflater by lazy { LayoutInflater.from(context) }
    init {
        orientation = VERTICAL
    }

    @SuppressLint("SetTextI18n")
    fun setData(carts: List<Cart>, cardMethod: CardMethod) {
        removeAllViews()
        carts.forEach { cart ->
            val binding = ItemCheckoutProductViewBinding.inflate(inflater)
            binding.tvName.text = buildString {
                append("${cart.count}x${cart.product.name}")
                append(cart.getVariationAndModifier())
            }
            binding.tvTotalPrice.text = "1x ${NumberFormater.formatNumberLocale(cart.getPricePerItem(cardMethod = cardMethod))}"
            addView(binding.root)
        }
    }
}