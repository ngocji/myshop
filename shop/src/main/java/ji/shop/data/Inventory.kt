package ji.shop.data

data class Inventory(
    val image: Int,
    val name: String,
    val isVisibility: Boolean,
    val orders: Int,
    val complete: Double,
    val price: Double,
    val sold: Double,
    val remaining: Int,
    val quantity: Double
)
