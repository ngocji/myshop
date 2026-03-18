package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.ProductModifier

data class ModifierDto(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("position")
    val position: Int?,

    @SerializedName("options")
    val options: List<ModifierOptionDto>?
)

fun ModifierDto.toDomain(): ProductModifier {
    return ProductModifier(
        id = id.orEmpty(),
        name = name.orEmpty(),
        position = position ?: 0,
        options = options
            ?.map { it.toDomain() }
            ?.sortedBy { it.position }
            ?: emptyList()
    )
}