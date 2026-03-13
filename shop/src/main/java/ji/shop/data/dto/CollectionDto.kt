package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class CollectionDto(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("sort")
    val sort: Int?,

    @SerializedName("groups")
    val groups: List<GroupDto>?
)

fun CollectionDto.toDomain(): ji.shop.data.domain.Collection {
    return ji.shop.data.domain.Collection(
        id = id.orEmpty(),
        name = name.orEmpty(),
        image = listOf(groups?.firstOrNull()?.imageUrl.orEmpty()),
        groups = groups?.map { it.toDomain(id.orEmpty()) } ?: emptyList(),
    )
}