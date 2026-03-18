package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class WrapResponse<T>(
    @SerializedName("success")
    val isSuccess: Boolean,
    @SerializedName("data", alternate = ["temporary_fees"])
    val data: T?
)