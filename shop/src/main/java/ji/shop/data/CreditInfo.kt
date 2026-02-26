package ji.shop.data

import ji.shop.R

class CreditInfo {

}

sealed class CardMethod(val id: Int) {
    class CardSwiper : CardMethod(R.string.text_card_swiper)
    class CardManually : CardMethod(R.string.text_card_manually)
    class Cash : CardMethod(R.string.text_cash)
    class Comp : CardMethod(R.string.text_comp)

    companion object {
        fun all() = listOf(CardSwiper(), CardManually(), Cash(), Comp())
    }
}
