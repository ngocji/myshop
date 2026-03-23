package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.Refund
import ji.shop.data.domain.RefundItem
import ji.shop.data.domain.Summary

data class RefundDto(
    @SerializedName("order")
    val order: OrderInfoDto?,

    @SerializedName("items")
    val items: List<RefundItemDto>?,

    @SerializedName("summary")
    val summary: SummaryRefundDto?,
)

data class RefundItemDto(
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
    @SerializedName("price")
    val price: Double?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("variation_name")
    val variationName: String?,
    @SerializedName("modifiers")
    val modifiers: List<ModifierDto>?
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
        order = order?.toDomains(),
        items = items?.map { it.toDomain() } ?: emptyList(),
        summary = summary?.toDomain()
    )
}

fun RefundItemDto.toDomain(): RefundItem {
    return RefundItem(
        posOrderItemId = posOrderItemId ?: -1,
        name = name.orEmpty(),
        quantity = quantity ?: 0,
        quantityRefundable = quantityRefundable ?: 0,
        price = price ?: 0.0,
        currencySymbol = currencySymbol.orEmpty(),
        isTicket = isTicket ?: false,
        modifiers = modifiers?.map { it.toDomain() } ?: emptyList()
    )
}

fun SummaryRefundDto.toDomain(): Summary {
    return Summary(
        refundableAmount = refundableAmount ?: 0.0,
        refundableAmountRaw = refundableAmountRaw ?: 0.0,
        currencySymbol = currencySymbol.orEmpty()
    )
}
