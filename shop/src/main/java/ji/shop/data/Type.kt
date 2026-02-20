package ji.shop.data

import ji.shop.R

enum class Type(val titleRes: Int) {
    Sell(R.string.text_sell),
    Inventory(R.string.text_inventory),
    Orders(R.string.text_orders)
}