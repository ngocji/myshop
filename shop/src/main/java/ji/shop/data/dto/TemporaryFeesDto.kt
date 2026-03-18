package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.TemporaryFees

data class TemporaryFeesDto(
    @SerializedName("quantity_error")
    val quantityError: String? = null,

    @SerializedName("coupon_error")
    val couponError: String? = null,

    @SerializedName("coupon_discount")
    val couponDiscount: Double? = null,

    @SerializedName("base_price")
    val basePrice: Double? = null,

    @SerializedName("est_fee")
    val estFee: Double? = null,

    @SerializedName("sub_total")
    val subTotal: Double? = null,

    @SerializedName("est_sales_tax")
    val estSalesTax: Double? = null,

    @SerializedName("total_money")
    val totalMoney: Double? = null,

    @SerializedName("total_ticket")
    val totalTicket: Int? = null
)

fun TemporaryFeesDto.toDomain(): TemporaryFees {
    return TemporaryFees(
        quantityError = quantityError.orEmpty(),
        couponError = couponError.orEmpty(),
        couponDiscount = couponDiscount ?: 0.0,
        basePrice = basePrice ?: 0.0,
        estFee = estFee ?: 0.0,
        subTotal = subTotal ?: 0.0,
        estSalesTax = estSalesTax ?: 0.0,
        totalMoney = totalMoney ?: 0.0,
        totalTicket = totalTicket ?: 0
    )
}