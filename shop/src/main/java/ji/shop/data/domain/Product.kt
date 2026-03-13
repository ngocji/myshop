package ji.shop.data.domain

data class Product(
    val id: String,
    val groupId: String,
    val collectionId: String,
    val name: String,
    val price: Double,
    val status: Status,
    val description: String,
    val images: List<Any?>,
    val sizes: List<ProductSize>,
    val additional: List<ProductAdditional>
) {
    fun isSingleSelection() = sizes.size <= 1 && additional.isEmpty()
}

data class ProductSize(
    val name: String,
    val price: Double
)

data class ProductAdditional(
    val name: String,
    val price: Double
)