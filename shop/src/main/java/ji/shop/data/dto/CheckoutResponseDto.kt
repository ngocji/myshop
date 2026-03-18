package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.CheckoutResult

data class CheckoutResponseDto(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("client_secret")
    val clientSecret: String? = null,

    @SerializedName("ticket_orders")
    val ticketOrders: Map<String, TicketOrderDto>? = null,

    @SerializedName("is_free_order")
    val isFreeOrder: Boolean? = null,

    @SerializedName("cart_id")
    val cartId: String? = null,

    @SerializedName("cart_private_code")
    val cartPrivateCode: String? = null,

    @SerializedName("has_ticket_pdf")
    val hasTicketPdf: Boolean? = null,

    @SerializedName("total_amount")
    val totalAmount: String? = null,

    @SerializedName("net_amount")
    val netAmount: String? = null,

    @SerializedName("total_points")
    val totalPoints: Int? = null
)

fun CheckoutResponseDto.toDomain() = CheckoutResult(
    isSuccess = success ?: false,
    message = message
)