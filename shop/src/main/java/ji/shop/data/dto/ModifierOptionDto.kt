package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.ModifierOption

data class ModifierOptionDto(

    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("price")
    val price: Double?,

    @SerializedName("position")
    val position: Int?,

    @SerializedName("is_public")
    val isPublic: Boolean?
)

fun ModifierOptionDto.toDomain(): ModifierOption {
    return ModifierOption(
        id = id.orEmpty(),
        name = name.orEmpty(),
        price = price ?: 0.0,
        position = position ?: 0,
        isPublic = isPublic ?: false
    )
}