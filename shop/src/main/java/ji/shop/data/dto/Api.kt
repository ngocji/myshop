package ji.shop.data.dto

import ji.shop.exts.buildApiService
import ji.shop.exts.buildOkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("api/v2/pos/get_pos_sell_hierarchy")
    suspend fun getSellHierarchy(
        @Query("pos_shop_id") posShopId: String,
        @Query("venue_id") venueId: String,
        @Query("auth_token") authToken: String
    ): WrapResponse<SellDataDto>

    companion object {
        fun create(): Api {
            return buildApiService(
                "https://api-staging.showslinger.com/",
                buildOkHttpClient(enableLog = true, HeaderInterceptor())
            )
        }
    }
}
