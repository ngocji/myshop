package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class WrapResponse<T>(
    @SerializedName("success")
    val isSuccess: Boolean,
    @SerializedName("data")
    val data: T?
)