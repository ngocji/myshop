package ji.shop.data

import ji.shop.R
import ji.shop.data.domain.Cart
import ji.shop.data.domain.Checkout
import ji.shop.data.domain.CreditInfo
import ji.shop.data.domain.CustomerInfo
import ji.shop.data.domain.Inventory
import ji.shop.data.domain.Product
import ji.shop.data.domain.ProductModifier
import ji.shop.data.domain.ProductVariation
import ji.shop.data.domain.ShopCategory
import ji.shop.data.domain.Status
import ji.shop.data.domain.Ticket
import ji.shop.data.dto.Api
import ji.shop.data.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repo {
    private val api by lazy {
        Api.create()
    }

    suspend fun getShopCategory() = withContext(Dispatchers.IO) {
        buildList {
            add(
                ShopCategory(
                    id = "1",
                    venueId = "90",
                    name = "Only Items"
                )
            )

            add(
                ShopCategory(
                    id = "2",
                    venueId = "90",
                    name = "Collections"
                )
            )
        }
    }

    suspend fun getSellData(
        posShopId: String,
        venueId: String
    ) = withContext(Dispatchers.IO) {
        api.getSellHierarchy(
            posShopId = posShopId,
            venueId = venueId
        ).data?.toDomain()
    }

    suspend fun getInventories() = withContext(Dispatchers.IO) {
        buildList {
            repeat(8) {
                add(
                    Inventory(
                        image = R.drawable.ic_inventory,
                        "Coca Cola $it",
                        false,
                        28,
                        2.291,
                        193.0,
                        2.291,
                        (20..100).random(),
                        2.291
                    )
                )
            }
        }
    }

    suspend fun getOrder(collectionId: String) = withContext(Dispatchers.IO) {
        Checkout(
            "c_",
            items = buildList {
                repeat(10) {
                    add(
                        Cart(
                            product = Product(
                                id = "p_$it",
                                name = "Product $it",
                                price = 90.0,
                                description = "Description $it",
                                images = listOf(
                                    R.drawable.ic_inventory,
                                    R.drawable.ic_inventory,
                                    R.drawable.ic_inventory,
                                    R.drawable.ic_inventory
                                ),
                                variations = listOf(
                                    ProductVariation("Small", 9.0),
                                    ProductVariation("Medium", 10.0),
                                    ProductVariation("Large", 12.0),
                                    ProductVariation("Extra Large", 14.0),
                                ),
                                status = Status.entries.toTypedArray().random(),
                                modifiers = listOf(
                                    ProductModifier(
                                        name = "Mild Sauce",
                                        price = 1.0
                                    ),
                                    ProductModifier(
                                        name = "Hot Sauce",
                                        price = 1.0
                                    ),
                                    ProductModifier(
                                        name = "Xtreme Sauce",
                                        price = 2.0
                                    )
                                )
                            ),
                            count = 3
                        )
                    )
                }
            },
            customerInfo = CustomerInfo("Bill Evans", "bill.evans@gmail.com", "678-774-0987"),
            creditInfo = CreditInfo(cardNumber = "678-774-0987")
        )
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