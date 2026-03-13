package ji.shop.data.domain

import ji.shop.R

enum class TabType(val titleRes: Int) {
    Sell(R.string.text_sell),
    Inventory(R.string.text_inventory),
    Orders(R.string.text_orders);
}