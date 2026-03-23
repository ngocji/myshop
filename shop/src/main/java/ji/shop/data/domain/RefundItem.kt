package ji.shop.data.domain

data class RefundItem(
    val posOrderItemId: Int,
    val name: String,
    val variationName: String = "",
    val imageUrl: String = "",
    val quantity: Int,
    val quantityRefundable: Int,
    val unitPrice: Double,
    val currencySymbol: String,
    val isTicket: Boolean,
    val refundQuantity: Int = 0
)
