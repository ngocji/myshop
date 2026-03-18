package ji.shop.data

import ji.shop.R
import ji.shop.ShopSDK
import ji.shop.data.domain.Cart
import ji.shop.data.domain.Checkout
import ji.shop.data.domain.CreditInfo
import ji.shop.data.domain.CustomerInfo
import ji.shop.data.domain.Inventory
import ji.shop.data.domain.Product
import ji.shop.data.domain.ProductModifier
import ji.shop.data.domain.ProductVariation
import ji.shop.data.domain.Status
import ji.shop.data.domain.Ticket
import ji.shop.data.dto.Api
import ji.shop.data.dto.RequestRefund
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

    suspend fun getInventories() = withContext(Dispatchers.IO) {
        api.getInventories(ShopSDK.getVenueId()).data?.map { it.toDomain() } ?: emptyList()
    }

    suspend fun getOrder(posItemId: String?) = withContext(Dispatchers.IO) {
        api.getOrders(posItemId, ShopSDK.getVenueId()).data?.map { it.toDomain() }
    }

    suspend fun getRefund(posOrderId: String?) = withContext(Dispatchers.IO) {
        api.getRefundInformation(posOrderId, ShopSDK.getVenueId()).data?.toDomain()
    }

    suspend fun refundPosOrder(refund: RequestRefund?) = withContext(Dispatchers.IO) {
        api.refundPosOrder(refund)
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
}