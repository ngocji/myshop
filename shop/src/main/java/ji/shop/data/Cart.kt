package ji.shop.data

data class Cart(
    val product: Product,
    val size: ProductSize,
    val count: Int,
    val additional: Map<ProductAdditional, Int>
)