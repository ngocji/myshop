package ji.shop.data.domain

data class ViewOrder(
    val orderInfo: OrderInfo?,
    val items: List<Item>,
    val summary: SummaryViewOrder?
)


data class SummaryViewOrder(
    val currencySymbol: String,
    val itemsCount: Int,
    val paymentText: String,
    val subtotal: Double,
    val tax: Double,
    val total: Double,
    val couponCode: String = "",
    val discountType: String = "",
    val discountValue: String = "",
    val discountAmount: Int = 0,
    val discountAmountRaw: Double = 0.0
)

data class OrderInfo(
    val buyerEmail: String,
    val buyerName: String,
    val buyerPhone: String,
    val paymentMethod: String,
    val posOrderId: String,
    val time: String
)