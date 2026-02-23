package ji.shop.data

data class Cart(
    val product: Product,
    val size: ProductSize?,
    val count: Int,
    val additional: Map<ProductAdditional, Int>
) {
    fun getTotalPrice(count: Int = this.count): Double {
        val perItem = product.price + (size?.price ?: 0.0) + additional.map { it.key.price * it.value }.sum()
        return perItem * count
    }
}