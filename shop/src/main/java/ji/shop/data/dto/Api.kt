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

    @GET("api/v2/xxx")
    suspend fun refreshToken(refreshToken: String): WrapResponse<TokenDto>

    companion object {
        lateinit var api: Api
        fun create(): Api {
            if (this::api.isInitialized) return api
            api = buildApiService(
                "https://api-staging.showslinger.com/",
                buildOkHttpClient(
                    enableLog = true,
                    authenticator = RefreshTokenAuth(),
                    HeaderInterceptor()
                )
            )
            return api
        }
    }
}
