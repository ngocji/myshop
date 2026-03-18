package ji.shop.data.domain

data class Cart(
    var generatedId: String? = null,
    var shop: ShopCategory? = null,
    val product: Product,
    val variation: ProductVariation? = null,
    val count: Int,
    val date: Long = System.currentTimeMillis(),
    val modifiers: Map<ProductModifier, WrapperOptionModifier> = emptyMap()
) {
    fun compute(shop: ShopCategory?) {
        this.shop = shop
        generatedId = buildString {
            append(shop?.posShopId)
            append(product.id)
            append(variation?.id)
            append(modifiers.keys.joinToString { it.id })
            append(modifiers.values.joinToString { it.toString() })
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Cart) {
            val isDiff = shop?.posShopId != other.shop?.posShopId  ||
                    product.id != other.product.id ||
                    variation?.id != other.variation?.id ||
                    modifiers.keys.map { it.id }.sorted() != other.modifiers.keys.map { it.id }.sorted() ||
                    modifiers.values.joinToString { it.toString() } != other.modifiers.values.joinToString { it.toString() }
            return !isDiff
        }
        return super.equals(other)
    }
    fun getTotalPrice(count: Int = this.count): Double {
        val perItem = product.price + (variation?.price ?: 0.0) + modifiers.map { it.value.getTotalPrice() }.sum()
        return perItem * count
    }

    override fun hashCode(): Int {
        var result = count
        result = 31 * result + date.hashCode()
        result = 31 * result + product.hashCode()
        result = 31 * result + (variation?.hashCode() ?: 0)
        result = 31 * result + modifiers.hashCode()
        return result
    }
}