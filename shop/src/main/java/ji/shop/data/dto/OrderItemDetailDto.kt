package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.OrderItemDetail

data class OrderItemDetailDto(
    @SerializedName("pos_order_item_id")
    val posOrderItemId: Int? = null,

    @SerializedName("pos_item_id")
    val posItemId: Int? = null,

    @SerializedName("order_item_name")
    val orderItemName: String? = null,

    @SerializedName("variation_name")
    val variationName: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("unit_price")
    val unitPrice: Double? = null,

    @SerializedName("currency_symbol")
    val currencySymbol: String? = null,

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("is_modifier")
    val isModifier: Boolean? = null,

    @SerializedName("modifier_name")
    val modifierName: String? = null,

    @SerializedName("modifier_price")
    val modifierPrice: Double? = null,

    @SerializedName("modifier_belong_type")
    val modifierBelongType: String? = null
)

fun OrderItemDetailDto.toDomain(): OrderItemDetail {
    return OrderItemDetail(
        posOrderItemId = posOrderItemId ?: 0,
        posItemId = posItemId ?: 0,
        orderItemName = orderItemName.orEmpty(),
        variationName = variationName.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        unitPrice = unitPrice ?: 0.0,
        currencySymbol = currencySymbol.orEmpty(),
        quantity = quantity ?: 0,
        isModifier = isModifier ?: false,
        modifierName = modifierName.orEmpty(),
        modifierPrice = modifierPrice ?: 0.0,
        modifierBelongType = modifierBelongType.orEmpty()
    )
}