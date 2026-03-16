package ji.shop.data.domain

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val status: Status,
    val description: String,
    val images: List<Any?>,
    val variations: List<ProductVariation>,
    val modifiers: List<ProductModifier>
) {
    fun isSingleSelection() = variations.size <= 1 && modifiers.isEmpty()
}

data class ProductVariation(
    val name: String,
    val price: Double
)

data class ProductModifier(
    val name: String,
    val price: Double
)