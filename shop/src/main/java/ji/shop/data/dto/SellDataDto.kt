package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.SellData

data class SellDataDto(
    @SerializedName("mode")
    val mode: String? = "",
    @SerializedName("collections")
    val collections: List<CollectionDto>? = null,
    @SerializedName("items")
    val items: List<ProductDto>? = null,
    @SerializedName("groups")
    val groups: List<GroupDto>? = null
)

fun SellDataDto.toDomain() = SellData(
    collections = collections?.map { it.toDomain() },
    items = items?.map { it.toDomain() },
    groups = groups?.map { it.toDomain() }
)