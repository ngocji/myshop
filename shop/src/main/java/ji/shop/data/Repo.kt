package ji.shop.data

import ji.shop.R
import ji.shop.ShopSDK
import ji.shop.data.domain.CardMethod
import ji.shop.data.domain.Cart
import ji.shop.data.domain.CheckoutResult
import ji.shop.data.domain.CreditInfo
import ji.shop.data.domain.CustomerInfo
import ji.shop.data.domain.Refund
import ji.shop.data.domain.RefundItem
import ji.shop.data.domain.Ticket
import ji.shop.data.domain.WrapPager
import ji.shop.data.dto.Api
import ji.shop.data.dto.createRefundRequest
import ji.shop.data.dto.createShoppingCartRequest
import ji.shop.data.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repo {
    private val api by lazy {
        Api.create()
    }

    suspend fun getShopCategories() = withContext(Dispatchers.IO) {
        api.getShopCategories(ShopSDK.getVenueId()).data?.map { it.toDomain() }
    }

    suspend fun getSellData(posShopId: String) = withContext(Dispatchers.IO) {
        api.getSellHierarchy(
            posShopId = posShopId,
            venueId = ShopSDK.getVenueId()
        ).data?.toDomain()
    }

    suspend fun getInventories(posShopId: String) = withContext(Dispatchers.IO) {
        api.getInventories(ShopSDK.getVenueId(), posShopId).data?.map { it.toDomain() }
            ?: emptyList()
    }

    suspend fun getOrder(posShopId: String, page: Int, limit: Int) = withContext(Dispatchers.IO) {
        val items =
            api.getOrders(posShopId, ShopSDK.getVenueId(), page, limit).data?.map { it.toDomain() }
                ?: emptyList()
        WrapPager(
            items = items,
            page = page,
            isEnded = items.size < limit
        )
    }

    suspend fun getRefund(posOrderId: String?) = withContext(Dispatchers.IO) {
        api.getRefundInformation(posOrderId, ShopSDK.getVenueId()).data?.toDomain()
    }

    suspend fun getViewOrder(posOrderId: String?) = withContext(Dispatchers.IO) {
        api.getViewOrder(posOrderId.orEmpty(), ShopSDK.getVenueId()).data?.toDomain()
    }

    suspend fun getOrderDetailItems(posOrderId: String?) = withContext(Dispatchers.IO) {
        api.getDetailOrder(
            posOrderId.orEmpty(),
            ShopSDK.getVenueId()
        ).data?.items?.map { it.toDomain() }
    }

    suspend fun getCouponsReport(posOrderId: String?) = withContext(Dispatchers.IO) {
        api.getCouponsReport(posOrderId, ShopSDK.getVenueId()).data?.toDomain()
    }

    suspend fun refundPosOrder(refund: Refund, items: List<RefundItem>) =
        withContext(Dispatchers.IO) {
            api.refundPosOrder(createRefundRequest(refund, items))
        }

    suspend fun getTicket() = withContext(Dispatchers.IO) {
        Ticket(
            image = R.drawable.ic_card,
            name = "Summer Music Fest",
            date = System.currentTimeMillis(),
            ticketDayPass = "Festival Day Pass",
            info = mapOf(
                "Coupon Discount" to 0.0,
                "Face Value" to 1.0,
                "Donation" to 1.0,
                "Service Fee" to 1.0,
                "Subtotal" to 1.0,
                "Taxes" to 1.0,
                "Total" to 30.0
            )
        )
    }

    fun getLastUsedCreditCard(): CreditInfo? {
        return CreditInfo(cardNumber = "678-774-0987")
    }

    fun getLastCustomerInfo(): CustomerInfo? {
        return CustomerInfo(
            name = "Nguyen Van A",
            email = "text@mail.com",
            phoneNumber = "0000000000"
        )
    }

    suspend fun getTemporaryShoppingCart(carts: List<Cart>, cardMethod: CardMethod) =
        withContext(Dispatchers.IO) {
            api.getTemporaryShoppingCartFees(
                createShoppingCartRequest(
                    carts = carts,
                    cardMethod = cardMethod
                )
            ).data?.toDomain()
        }

    suspend fun createShoppingCart(
        carts: List<Cart>,
        cardMethod: CardMethod,
        creditInfo: CreditInfo?,
        customerInfo: CustomerInfo?
    ) =
        withContext(Dispatchers.IO) {
            val checkoutResponse = api.createShoppingCartOrder(
                createShoppingCartRequest(
                    carts = carts,
                    cardMethod = cardMethod,
                    creditInfo = creditInfo,
                    customerInfo = customerInfo ?: getLastCustomerInfo()
                )
            )
            if (checkoutResponse?.success == true &&
                !checkoutResponse.cartId.isNullOrBlank() &&
                cardMethod == CardMethod.Cash
            ) {
                val processResponse = api.processCompleteCart(cartId = checkoutResponse.cartId)
                CheckoutResult(
                    isSuccess = processResponse.isSuccess,
                )
            } else {
                checkoutResponse?.toDomain()
            }
        }
}