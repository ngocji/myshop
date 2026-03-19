package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.OrderInfo
import ji.shop.data.domain.SummaryViewOrder
import ji.shop.data.domain.ViewOrder

data class ViewOrderDto(

    @SerializedName("order")
    val order: OrderInfoDto?,

    @SerializedName("summary")
    val summary: SummaryDto,

    @SerializedName("items")
    val items: List<ItemDto>?
)

data class OrderInfoDto(
    @SerializedName("pos_order_id")
    val posOrderId: String?,

    @SerializedName("buyer_name")
    val buyerName: String?,

    @SerializedName("buyer_email")
    val buyerEmail: String?,

    @SerializedName("buyer_phone")
    val buyerPhone: String?,

    @SerializedName("payment_method")
    val paymentMethod: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("time")
    val time: String?,

    @SerializedName("payment_text")
    val paymentText: String?
)

data class SummaryDto(
    @SerializedName("items_count")
    val itemsCount: Int?,

    @SerializedName("subtotal")
    val subtotal: Double?,

    @SerializedName("tax")
    val tax: Double?,

    @SerializedName("total")
    val total: Double?,

    @SerializedName("currency_symbol")
    val currencySymbol: String?,

    @SerializedName("payment_text")
    val paymentText: String?,
)

fun ViewOrderDto.toDomain() : ViewOrder {
    return ViewOrder(
        orderInfo = order?.toDomains(),
        items = items?.map { it.toDomain() } ?: emptyList(),
        summary = summary.toDomain()
    )
}

fun SummaryDto.toDomain() : SummaryViewOrder {
    return SummaryViewOrder(
        currencySymbol = currencySymbol.orEmpty(),
        itemsCount = itemsCount ?: 0,
        paymentText = paymentText.orEmpty(),
        subtotal = subtotal ?: 0.0,
        tax = tax ?: 0.0,
        total = total ?: 0.0
    )
}

fun OrderInfoDto.toDomains() : OrderInfo {
    return OrderInfo(
        buyerEmail = buyerEmail.orEmpty(),
        buyerName = buyerName.orEmpty(),
        buyerPhone = buyerPhone.orEmpty(),
        paymentMethod = paymentMethod.orEmpty(),
        posOrderId = posOrderId.orEmpty(),
        time = time.orEmpty()
    )
}
