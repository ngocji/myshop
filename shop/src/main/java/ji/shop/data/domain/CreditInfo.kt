package ji.shop.data.domain

import ji.shop.R

data class CreditInfo(
    val cardNumber: String,
    val nameOnCard: String = "",
    val date: String = "",
    val cvv: String = "",
    val imageUri: String? = null
)

sealed class CardMethod(val id: Int, val name: String = "") {
    object CardSwiper : CardMethod(R.string.text_card_swiper)
    object CardManually : CardMethod(R.string.text_card_manually)
    object Cash : CardMethod(R.string.text_cash, "cash")
    object Comp : CardMethod(R.string.text_comp)

    object Credit : CardMethod(R.string.text_credit, "online")
    companion object {
        fun all() = listOf(CardSwiper, CardManually, Cash, Comp)
    }
}
