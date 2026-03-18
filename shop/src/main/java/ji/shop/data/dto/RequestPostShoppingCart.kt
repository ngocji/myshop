package ji.shop.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import ji.shop.ShopSDK
import ji.shop.data.domain.CardMethod
import ji.shop.data.domain.Cart
import ji.shop.utils.Log

data class RequestPostShoppingCart(
    @SerializedName("discountCode")
    val discountCode: String? = null,

    @SerializedName("checkout_source")
    val checkoutSource: String? = null,

    @SerializedName("device_info")
    val deviceInfo: DeviceWrapperRequest? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("information")
    val information: InformationRequest? = null,

    @SerializedName("is_remember_reader")
    val isRememberReader: Boolean? = null,

    @SerializedName("payment_method")
    val paymentMethod: String? = null,

    @SerializedName("shopping_cart")
    val shoppingCart: Map<String, ShopCartRequest>? = null,

    @SerializedName("venue_id")
    val venueId: String? = null
)

data class DeviceWrapperRequest(
    @SerializedName("device_info")
    val deviceInfo: DeviceInfoRequest? = null
)

data class DeviceInfoRequest(
    @SerializedName("app_version")
    val appVersion: String? = null,

    @SerializedName("device_name")
    val deviceName: String? = null,

    @SerializedName("device_os")
    val deviceOs: String? = null,

    @SerializedName("version_os")
    val versionOs: String? = null
)

data class InformationRequest(
    @SerializedName("address")
    val address: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("postal_code")
    val postalCode: String? = null,

    @SerializedName("state")
    val state: String? = null
)

data class ShopCartRequest(
    @SerializedName("object_cart")
    val objectCart: ObjectCartRequest? = null,

    @SerializedName("pos_shop_image")
    val posShopImage: String? = null,

    @SerializedName("pos_shop_name")
    val posShopName: String? = null
)

data class ObjectCartRequest(
    @SerializedName("tickets")
    val tickets: Map<String, TicketRequest>? = null
)

data class TicketRequest(
    @SerializedName("cashPrice")
    val cashPrice: Double? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("onlinePrice")
    val onlinePrice: Double? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("ticketDetailId")
    val ticketDetailId: String? = null,

    @SerializedName("total")
    val total: Int? = null,

    @SerializedName("variation")
    val variation: List<VariationItemRequest>? = null,

    @SerializedName("modifier_options")
    val modifierOptions: List<ModifierOptionItemRequest>? = null
)

data class VariationItemRequest(
    @SerializedName("variation_id")
    val variationId: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("onlinePrice")
    val onlinePrice: Double? = null,

    @SerializedName("cashPrice")
    val cashPrice: Double? = null,

    @SerializedName("total")
    val total: Int? = null
)

data class ModifierOptionItemRequest(
    @SerializedName("belong_to_variation_id")
    val belongToVariationId: String? = null,

    @SerializedName("modifier_option_id")
    val modifierOptionId: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("total")
    val total: Int? = null
)

fun createShoppingCartRequest(carts: List<Cart>, cardMethod: CardMethod): RequestPostShoppingCart {
    val shopCartsMap = mutableMapOf<String, ShopCartRequest>()
    carts.groupBy { it.shop }.forEach { (shop, shopCarts) ->
        if (shop != null) {
            val ticketsMap = mutableMapOf<String, TicketRequest>()
            shopCarts.forEach { cart ->
                val ticketRequest = TicketRequest(
                    cashPrice = cart.product.cashPrice,
                    name = cart.product.name,
                    onlinePrice = cart.product.onlinePrice,
                    price = cart.product.price,
                    ticketDetailId = cart.product.id,
                    total = cart.count,
                    variation = cart.variation?.let {
                        listOf(
                            VariationItemRequest(
                                variationId = it.id,
                                name = it.name,
                                onlinePrice = it.onlinePrice,
                                cashPrice = it.cashPrice,
                                total = cart.count
                            )
                        )
                    },
                    modifierOptions = cart.modifiers.flatMap { (_, wrapper) ->
                        wrapper.items.map { (option, count) ->
                            ModifierOptionItemRequest(
                                belongToVariationId = cart.variation?.id,
                                modifierOptionId = option.id,
                                name = option.name,
                                price = option.price,
                                total = count
                            )
                        }
                    }
                )
                ticketsMap[cart.product.id] = ticketRequest
            }
            shopCartsMap[shop.posShopId] = ShopCartRequest(
                objectCart = ObjectCartRequest(tickets = ticketsMap),
                posShopName = shop.name,
                posShopImage = ""
            )
        }
    }
    return RequestPostShoppingCart(
        shoppingCart = shopCartsMap,
        paymentMethod = cardMethod.name,
        venueId = ShopSDK.getVenueId()
    ).also {
        Log.d("Request shoping cart:\n${Gson().toJson(it)}")
    }
}
