package ji.shop.data.domain

data class RefundItem(
    val posOrderItemId: Int,
    val name: String,
    val variationName: String = "",
    val imageUrl: String = "",
    val quantity: Int,
    val quantityRefundable: Int,
    val price: Double,
    val currencySymbol: String,
    val isTicket: Boolean,
    val modifiers: List<ProductModifier>,
    val refundQuantity: Int = 0
) {
    fun getTotalModifierPrice() : Double {
        return price + modifiers.sumOf { price }
    }
}
