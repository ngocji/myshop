package ji.shop.data.domain

data class WrapPager<T>(
    val items: List<T>,
    val page: Int,
    val isEnded: Boolean,
    val allItems: List<T> = emptyList(),
)