package ji.shop.data.dto

import ji.shop.exts.buildApiService
import ji.shop.exts.buildOkHttpClient
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @GET("pos/get_pos_shops_by_venue")
    suspend fun getShopCategories(@Query("venue_id") venueId: String): WrapResponse<List<ShopCategoryDto>>

    @GET("pos/get_pos_sell_hierarchy")
    suspend fun getSellHierarchy(
        @Query("pos_shop_id") posShopId: String,
        @Query("venue_id") venueId: String
    ): WrapResponse<SellDataDto>

    @GET("pos/get_pos_orders_by_shop")
    suspend fun getOrders(
        @Query("post_shop_id") posShopId: String?,
        @Query("venue_id") venueId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): WrapResponse<List<OrderDto>>

    @GET("pos/get_list_item_by_shop")
    suspend fun getInventories(
        @Query("venue_id") venueId: String,
        @Query("pos_shop_id") posShopId: String
    ): WrapResponse<List<InventoryDto>>

    @GET("pos/get_pos_order_detail")
    suspend fun getOrderDetail(
        @Query("pos_item_id") posItemId: String,
        @Query("venue_id") venueId: String
    ): WrapResponse<OrderDto>

    @GET("pos/get_pos_order_view")
    suspend fun getViewOrder(
        @Query("pos_order_id") posOrderId: String?,
        @Query("venue_id") venueId: String,
    ): WrapResponse<ViewOrderDto>

    @GET("pos/get_pos_coupons_report")
    suspend fun getCouponsReport(
        @Query("pos_order_id") posOrderId: String?,
        @Query("venue_id") venueId: String,
    ): WrapResponse<ViewOrderDto>

    @GET("pos/get_pos_refund_information")
    suspend fun getRefundInformation(
        @Query("pos_order_id") posOrderId: String?,
        @Query("venue_id") venueId: String,
    ): WrapResponse<RefundDto>

    @POST("pos/refund_pos_order")
    suspend fun refundPosOrder(
        @Body refund: RequestRefund?
    ): WrapResponse<RefundDto>

    @POST("/user/refresh_token")
    suspend fun refreshToken(@Body request: RequestGetRefreshToken): TokenDto?

    @POST("pos/get_pos_temporary_shopping_cart_fees")
    suspend fun getTemporaryShoppingCartFees(@Body request: RequestPostShoppingCart): WrapResponse<TemporaryFeesDto>

    @POST("pos/create_pos_shopping_cart_order")
    suspend fun createShoppingCartOrder(@Body request: RequestPostShoppingCart): CheckoutResponseDto?

    @PATCH("pos/process_complete_cart")
    suspend fun processCompleteCart(
        @Query("cart_id") cartId: String
    ): WrapResponse<Any>

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
