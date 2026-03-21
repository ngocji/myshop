package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.ShopCategory

data class ShopCategoryDto(
    @SerializedName("pos_item_id")
    val posItemId: String?, // todo remote

    @SerializedName("pos_shop_id")
    val posShopId: String?,

    @SerializedName("pos_shop_name")
    val posShopName: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("visibility")
    val visibility: Boolean?,

    @SerializedName("orders")
    val orders: Int?,

    @SerializedName("total_complete_orders")
    val totalCompleteOrders: Int?,

    @SerializedName("sold_quantity")
    val soldQuantity: Int?,

    @SerializedName("total_price")
    val totalPrice: Double?,

    @SerializedName("currency_symbol")
    val currencySymbol: String?,

    @SerializedName("inventory_remaining_percent")
    val inventoryRemainingPercent: Int?,

    @SerializedName("quantity_total")
    val quantityTotal: Int?,

    @SerializedName("inventory_remaining_quantity")
    val inventoryRemainingQuantity: Int?
)

fun ShopCategoryDto.toDomain() = ShopCategory(
    posShopId = posShopId ?: "",
    name = name ?: ""
)