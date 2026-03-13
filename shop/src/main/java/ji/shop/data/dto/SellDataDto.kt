package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class SellDataDto(
    @SerializedName("mode")
    val mode: String? = "",
    @SerializedName("collections")
    val collections: List<CollectionDto> = emptyList()
)