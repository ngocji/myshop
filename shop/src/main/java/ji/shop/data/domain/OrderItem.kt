package ji.shop.data.domain

data class OrderItem(
    val posOrderId: String,
    val posItemId: String,
    val quantity: Int,
    val imageUrl: String
)