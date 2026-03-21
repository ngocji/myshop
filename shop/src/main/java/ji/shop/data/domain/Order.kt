package ji.shop.data.domain

data class Order(
    val posItemId: String,
    val posOrderId: String,
    val name: String,
    val quantity: Int,
    val total: Double,
    val currencySymbol: String,
    val time: String,
    val paymentMethod: String,
    val status: Status,
    val items: List<OrderItem> = emptyList(),
    val images: List<String> = emptyList(),
)