package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class OrderDetailDto(
    @SerializedName("items")
    val items: List<OrderItemDetailDto>? = null
)