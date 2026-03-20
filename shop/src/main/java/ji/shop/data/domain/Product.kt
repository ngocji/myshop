package ji.shop.data.domain

data class Product(
    val id: String,
    val name: String,
    val onlinePrice: Double,
    val cashPrice: Double,
    val status: Status,
    val description: String,
    val images: List<Any?>,
    val variations: List<ProductVariation>,
    val modifiers: List<ProductModifier>,
    val isFavorite: Boolean,
    val visibility: Boolean,
    val isSoldOut: Boolean
) {
    val price get() = onlinePrice
    fun isSingleSelection() = variations.size <= 1 && modifiers.isEmpty()
}

data class ProductVariation(
    val id: String,
    val name: String,
    val onlinePrice: Double,
    val cashPrice: Double,
    val position: Int,
    val image: String,
    val quantityAvailable: Int,
    val originalQuantity: Int
) {
    val price get() = onlinePrice
}

data class ProductModifier(
    val id: String,
    val name: String,
    val position: Int,
    val options: List<ModifierOption>
)

data class ModifierOption(
    val id: String,
    val name: String,
    val price: Double,
    val position: Int,
    val isPublic: Boolean
)