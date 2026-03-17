package ji.shop.data.domain

data class ShopCategory(
    val posItemId: String,
    val posShopId: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}