package ji.shop.data.domain

data class Refund(
    val customerInfo: CustomerInfo?,
    val items: List<Item>,
    val summary: Summary?
)

data class Summary(
    val refundableAmount: Double,
    val refundableAmountRaw: Double,
    val currencySymbol: String,
)