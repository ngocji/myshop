package ji.shop.data.domain

data class Cart(
    val product: Product,
    val size: ProductSize? = null,
    val count: Int,
    val date: Long = System.currentTimeMillis(),
    val additional: Map<ProductAdditional, Int> = emptyMap()
) {
    fun getTotalPrice(count: Int = this.count): Double {
        val perItem = product.price + (size?.price ?: 0.0) + additional.map { it.key.price * it.value }.sum()
        return perItem * count
    }
}