package ji.shop.data.domain

data class WrapperOptionModifier(
    val items: List<Pair<ModifierOption, Int>> = emptyList()
) {
    fun getTotalPrice() = items.sumOf { (option, count) -> option.price * count }

    override fun toString(): String {
        return items.joinToString { "${it.first.id}-${it.second}" }
    }
}