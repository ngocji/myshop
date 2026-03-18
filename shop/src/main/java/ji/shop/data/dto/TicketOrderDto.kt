package ji.shop.data.dto

import com.google.gson.annotations.SerializedName

data class TicketOrderDto(
    @SerializedName("private_code")
    val privateCode: String? = null,

    @SerializedName("sharing_code")
    val sharingCode: String? = null,

    @SerializedName("event_name")
    val eventName: String? = null
)