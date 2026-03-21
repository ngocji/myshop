package ji.shop.data.domain

data class OrderItemDetail(
    val posOrderItemId: Int,
    val posItemId: Int,
    val orderItemName: String,
    val variationName: String,
    val imageUrl: String,
    val unitPrice: Double,
    val currencySymbol: String,
    val quantity: Int,
    val isModifier: Boolean,
    val modifierName: String,
    val modifierPrice: Double,
    val modifierBelongType: String
)