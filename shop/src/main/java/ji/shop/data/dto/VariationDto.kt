package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.ProductVariation

data class VariationDto(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("online_price")
    val onlinePrice: Double?,

    @SerializedName("cash_price")
    val cashPrice: Double?,

    @SerializedName("position")
    val position: Int?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("quantity_available")
    val quantityAvailable: Int?,

    @SerializedName("original_quantity")
    val originalQuantity: Int?
)

fun VariationDto.toDomain(): ProductVariation {
    return ProductVariation(
        id = id.orEmpty(),
        name = name.orEmpty(),
        onlinePrice = onlinePrice ?: 0.0,
        cashPrice = cashPrice ?: 0.0,
        position = position ?: 0,
        image = image.orEmpty(),
        quantityAvailable = quantityAvailable ?: 0,
        originalQuantity = originalQuantity ?: 0
    )
}