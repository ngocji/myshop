package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

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
