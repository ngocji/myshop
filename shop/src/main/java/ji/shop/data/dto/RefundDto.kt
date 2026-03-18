package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.CustomerInfo
import ji.shop.data.domain.Item
import ji.shop.data.domain.Refund
import ji.shop.data.domain.Summary

data class RefundDto(
    @SerializedName("order")
    val order: OrderInfoDto?,

    @SerializedName("items")
    val items: List<ItemDto>?,

    @SerializedName("summary")
    val summary: SummaryRefundDto?,
)

data class ItemDto(
    @SerializedName("currency_symbol")
    val currencySymbol: String?,
    @SerializedName("is_ticket")
    val isTicket: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pos_order_item_id")
    val posOrderItemId: Int?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("quantity_refundable")
    val quantityRefundable: Int?,
    @SerializedName("unit_price")
    val unitPrice: Double?
)

data class SummaryRefundDto(
    @SerializedName("currency_symbol")
    val currencySymbol: String?,
    @SerializedName("refundable_amount")
    val refundableAmount: Double?,
    @SerializedName("refundable_amount_raw")
    val refundableAmountRaw: Double?
)

fun RefundDto.toDomain(): Refund {
    return Refund(
        customerInfo = order?.toDomain(),
        items = items?.map { it.toDomain() } ?: emptyList(),
        summary = summary?.toDomain()
    )
}

fun OrderInfoDto.toDomain(): CustomerInfo {
    return CustomerInfo(
        name = buyerName.orEmpty(),
        email = buyerEmail.orEmpty(),
        phoneNumber = buyerPhone.orEmpty()
    )
}

fun ItemDto.toDomain(): Item {
    return Item(
        posOrderItemId = posOrderItemId ?: -1,
        name = name.orEmpty(),
        quantity = quantity ?: 0,
        quantityRefundable = quantityRefundable ?: 0,
        unitPrice = unitPrice ?: 0.0,
        currencySymbol = currencySymbol.orEmpty(),
        isTicket = isTicket ?: false
    )
}

fun SummaryRefundDto.toDomain(): Summary {
    return Summary(
        refundableAmount = refundableAmount ?: 0.0,
        refundableAmountRaw = refundableAmountRaw ?: 0.0,
        currencySymbol = currencySymbol.orEmpty()
    )
}
