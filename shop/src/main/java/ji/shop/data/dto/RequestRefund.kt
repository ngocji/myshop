package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.Item
import ji.shop.data.domain.Refund

data class RequestRefund(
    @SerializedName("custom_refund_amount")
    val customRefundAmount: Double?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("refund_data")
    val refundData: List<RefundData>?,
    @SerializedName("ticket_order_id")
    val ticketOrderId: String?,
    @SerializedName("venue_id")
    val venueId: String?
)

data class RefundData(
    @SerializedName("is_all_refund")
    val isAllRefund: Boolean?,
    @SerializedName("refund_quantity")
    val refundQuantity: Int?,
    @SerializedName("ticket_order_item_id")
    val ticketOrderItemId: Int?
)

fun Refund.toRequest(ticketOrderId: String?, venueId: String?): RequestRefund {
    return RequestRefund(
        customRefundAmount = summary?.refundableAmount,
        message = "",
        refundData = items.map { it.toRequest() },
        ticketOrderId = ticketOrderId,
        venueId = venueId
    )
}

fun Item.toRequest(): RefundData {
    return RefundData(
        isAllRefund = isTicket,
        refundQuantity = quantity,
        ticketOrderItemId = posOrderItemId
    )
}

