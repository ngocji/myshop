package ji.shop.data.dto

import com.google.gson.annotations.SerializedName
import ji.shop.data.domain.Product
import ji.shop.data.domain.Status

data class ProductDto(

    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("online_price")
    val onlinePrice: Double?,

    @SerializedName("cash_price")
    val cashPrice: Double?,

    @SerializedName("sort")
    val sort: Int?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("status")
    val status: Boolean?,

    @SerializedName("is_pause")
    val isPause: Boolean?
)

fun ProductDto.toDomain(
    collectionId: String,
    groupId: String,
): Product {
    return Product(
        id = id.orEmpty(),
        groupId = groupId,
        collectionId = collectionId,
        name = name.orEmpty(),
        price = onlinePrice ?: 0.0,
        // todo update status for product
        status = when {
            status == true && isPause == false -> Status.COMPLETE
            isPause == true -> Status.PAID
            else -> Status.IN_PROGRESS
        },
        description = description.orEmpty(),
        images = listOf(imageUrl),
        sizes = emptyList(),
        additional = emptyList()
    )
}