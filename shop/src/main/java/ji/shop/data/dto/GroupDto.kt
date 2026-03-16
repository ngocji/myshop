package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.Group

data class GroupDto(

    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("sort")
    val sort: Int?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("items")
    val items: List<ProductDto>?
)

fun GroupDto.toDomain(collectionId: String? = null): Group {
    return Group(
        id = id.orEmpty(),
        name = name.orEmpty(),
        collectionId = collectionId,
        products = items?.map { it.toDomain() } ?: emptyList()
    )
}