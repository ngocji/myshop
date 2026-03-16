package ji.shop.data.domain

data class Group(
    val id: String,
    val collectionId: String? = null,
    val name: String,
    val products: List<Product> = emptyList()
) {
    companion object {
        const val GROUP_ONLY_ITEM_ID = "group_only_item_id"
        fun Group?.isOnlyItem() = this?.id == GROUP_ONLY_ITEM_ID
    }
}