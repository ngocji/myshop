package ji.shop.data.domain

data class Refund(
    val order: OrderInfo?,
    val items: List<RefundItem>,
    val summary: Summary?
)

data class Summary(
    val refundableAmount: Double,
    val refundableAmountRaw: Double,
    val currencySymbol: String,
)