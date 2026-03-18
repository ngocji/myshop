package ji.shop.data.domain

data class TemporaryFees(
    val quantityError: String,
    val couponError: String,
    val couponDiscount: Double,
    val basePrice: Double,
    val estFee: Double,
    val subTotal: Double,
    val estSalesTax: Double,
    val totalMoney: Double,
    val totalTicket: Int
) {
    override fun toString(): String {
        return "TemporaryFees(quantityError='$quantityError', couponError='$couponError', couponDiscount=$couponDiscount, basePrice=$basePrice, estFee=$estFee, subTotal=$subTotal, estSalesTax=$estSalesTax, totalMoney=$totalMoney)"
    }
}