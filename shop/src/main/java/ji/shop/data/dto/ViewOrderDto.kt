package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class ViewOrderDto(

    @SerializedName("order")
    val order: OrderInfoDto?
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
    val time: String?
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
