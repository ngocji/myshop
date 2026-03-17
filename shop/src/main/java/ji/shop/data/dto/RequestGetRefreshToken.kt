package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class RequestGetRefreshToken(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)