package ji.shop.data

data class Checkout(
    val id: String,
    val items: List<Cart>,
    val customerInfo: CustomerInfo? = null,
    val creditInfo: CreditInfo? = null
)
