package ji.shop.data.domain

data class Collection(
    val id: String,
    val name: String,
    val image: List<Any?> = emptyList(),
    val groups: List<Group> = emptyList()
)