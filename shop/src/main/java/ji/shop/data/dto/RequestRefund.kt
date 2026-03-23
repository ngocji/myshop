package ji.shop.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import ji.shop.ShopSDK
import ji.shop.data.domain.Order
import ji.shop.data.domain.Refund
import ji.shop.data.domain.RefundItem
import ji.shop.utils.Log

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

fun createRefundRequest(refund: Refund, selectedItems: List<RefundItem>): RequestRefund {
    val total = selectedItems.sumOf { it.refundQuantity * it.unitPrice }
    return RequestRefund(
        customRefundAmount = total,
        message = "",
        refundData = selectedItems.map { it.toRequest() },
        ticketOrderId = refund.order?.posOrderId,
        venueId = ShopSDK.getVenueId()
    ).also {
        Log.d("${Gson().toJson(it)}")
    }
}

fun RefundItem.toRequest(): RefundData {
    return RefundData(
        isAllRefund = quantity == refundQuantity,
        refundQuantity = refundQuantity,
        ticketOrderItemId = posOrderItemId
    )
}

