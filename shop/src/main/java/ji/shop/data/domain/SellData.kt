package ji.shop.data.domain

data class SellData(
    val collections: List<Collection>? = null,
    val items: List<Product>? = null,
    val groups: List<Group>? = null
)