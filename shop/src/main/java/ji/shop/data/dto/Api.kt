package ji.shop.data.dto

import ji.shop.exts.buildApiService
import ji.shop.exts.buildOkHttpClient
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @GET("pos/get_pos_list_by_venue?page=1&limit=100")
    suspend fun getShopCategories(@Query("venue_id") venueId: String): WrapResponse<List<ShopCategoryDto>>

    @GET("pos/get_pos_sell_hierarchy")
    suspend fun getSellHierarchy(
        @Query("pos_shop_id") posShopId: String,
        @Query("venue_id") venueId: String
    ): WrapResponse<SellDataDto>

    @GET("pos/get_pos_orders_by_item")
    suspend fun getOrders(
        @Query("pos_item_id") posItemId: String,
        @Query("venue_id") venueId: String,
        @Query("auth_token") authToken: String
    ): WrapResponse<OrderDto>

    @GET("pos/get_pos_order_detail")
    suspend fun getOrderDetail(
        @Query("pos_item_id") posItemId: String,
        @Query("venue_id") venueId: String,
        @Query("auth_token") authToken: String
    ): WrapResponse<OrderDto>

    @GET("pos/get_pos_order_view")
    suspend fun getViewOrder(
        @Query("pos_order_id") posOrderId: String,
        @Query("venue_id") venueId: String,
        @Query("auth_token") authToken: String
    ): WrapResponse<ViewOrderDto>

    @POST("user/refresh_token")
    suspend fun refreshToken(@Body request: RequestGetRefreshToken): TokenDto?

    companion object {
        lateinit var api: Api
        fun create(): Api {
            if (this::api.isInitialized) return api
            api = buildApiService(
                "https://api-staging.showslinger.com/api/v2/",
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
