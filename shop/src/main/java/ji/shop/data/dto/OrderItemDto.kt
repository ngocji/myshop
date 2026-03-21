package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.OrderItem

data class OrderItemDto(
    @SerializedName("pos_order_id")
    val posOrderId: String? = null,

    @SerializedName("pos_item_id")
    val posItemId: String? = null,

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null
)

fun OrderItemDto.toDomain(): OrderItem {
    return OrderItem(
        posOrderId = posOrderId.orEmpty(),
        posItemId = posItemId.orEmpty(),
        quantity = quantity ?: 0,
        imageUrl = imageUrl.orEmpty()
    )
}