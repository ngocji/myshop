package ji.shop.data.domain

data class Inventory(
    val image: String?,
    val name: String?,
    var isVisibility: Boolean,
    val orders: Int,
    val complete: Int,
    val price: Double,
    val sold: Int   ,
    val remaining: Int,
    val quantity: Int
)
