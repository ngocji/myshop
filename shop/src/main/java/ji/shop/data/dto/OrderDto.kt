package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.Order
import ji.shop.data.domain.Status

data class OrderDto(

    @SerializedName("pos_order_id")
    val posOrderId: String?,

    @SerializedName("pos_item_id")
    val posItemId: String?,

    @SerializedName("buyer_name")
    val buyerName: String?,

    @SerializedName("quantity")
    val quantity: Int?,

    @SerializedName("total")
    val total: Double?,

    @SerializedName("currency_symbol")
    val currencySymbol: String?,

    @SerializedName("time")
    val time: String?,

    @SerializedName("payment_method")
    val paymentMethod: String?,

    @SerializedName("status")
    val status: String?,

    )

fun OrderDto.toDomain() : Order {
    return Order(
        posItemId = posItemId.orEmpty(),
        posOrderId = posOrderId.orEmpty(),
        name = buyerName.orEmpty(),
        quantity = quantity ?: 0,
        total = total ?: 0.0,
        currencySymbol = currencySymbol.orEmpty(),
        time = time.orEmpty(),
        paymentMethod = paymentMethod.orEmpty(),
        status = status?.toStatus() ?: Status.IN_PROGRESS
    )
}

fun String.toStatus() : Status {
    return Status.valueOf(this)
}

