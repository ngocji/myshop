package ji.shop.data.domain

data class ShopCategory(
    val id: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}