package ji.shop.data

import ji.shop.R
import ji.shop.data.domain.Cart
import ji.shop.data.domain.Checkout
import ji.shop.data.domain.Collection
import ji.shop.data.domain.CreditInfo
import ji.shop.data.domain.CustomerInfo
import ji.shop.data.domain.Group
import ji.shop.data.domain.Inventory
import ji.shop.data.domain.Product
import ji.shop.data.domain.ProductAdditional
import ji.shop.data.domain.ProductSize
import ji.shop.data.domain.ShopCategory
import ji.shop.data.domain.Status
import ji.shop.data.domain.Ticket
import ji.shop.data.dto.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

object Repo {
    private val api by lazy {
        Api.create()
    }

    suspend fun getShopCategory() = withContext(Dispatchers.IO) {
        buildList {
            add(
                ShopCategory(
                    id = "3",
                    name = "Festival Concessions"
                )
            )
        }
    }

    suspend fun getSellData(
        posShopId: String = "",
        venueId: String = ""
    ) = withContext(Dispatchers.IO) {
//        api.getSellHierarchy(
//            posShopId = posShopId,
//            venueId = venueId,
//            authToken = ShopSDK.getAuthenticationToken()
//        ).data?.collections?.map { it.toDomain() }
        buildList {
            repeat(5) {
                add(
                    Collection(
                        id = "c_$it",
                        name = "Collection $it",
                        image = listOf(R.drawable.ic_colection),
                        groups = fakeGroup("c_$it")
                    )
                )
            }
        }
    }

    private fun fakeGroup(collectionId: String): List<Group> {
        return buildList {
            repeat(5) {
                add(
                    Group(
                        id = "g_$it",
                        name = "Group $it",
                        collectionId = collectionId,
                        products = fakeProduct(collectionId, "g_$it")
                    )
                )
            }
        }
    }

    private fun fakeProduct(collectionId: String, groupId: String): List<Product> {
        return buildList {
            repeat(5) {
                add(
                    Product(
                        id = "p_$it",
                        groupId = groupId,
                        collectionId = collectionId,
                        name = "Product $it",
                        price = 90.0,
                        description = "Description $it",
                        images = listOf(R.drawable.ic_product),
                        sizes = listOf(
                            ProductSize("Small", 9.0),
                            ProductSize("Medium", 10.0),
                            ProductSize("Large", 12.0)
                        ),
                        status = Status.entries.toTypedArray().random(),
                        additional = emptyList()
                    )
                )
            }
        }
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
                                groupId = "g_$it",
                                collectionId = collectionId,
                                name = "Product $it",
                                price = 90.0,
                                description = "Description $it",
                                images = listOf(
                                    R.drawable.ic_inventory,
                                    R.drawable.ic_inventory,
                                    R.drawable.ic_inventory,
                                    R.drawable.ic_inventory
                                ),
                                sizes = listOf(
                                    ProductSize("Small", 9.0),
                                    ProductSize("Medium", 10.0),
                                    ProductSize("Large", 12.0),
                                    ProductSize("Extra Large", 14.0),
                                ),
                                status = Status.entries.toTypedArray().random(),
                                additional = listOf(
                                    ProductAdditional(
                                        name = "Mild Sauce",
                                        price = 1.0
                                    ),
                                    ProductAdditional(
                                        name = "Hot Sauce",
                                        price = 1.0
                                    ),
                                    ProductAdditional(
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

    fun getFavorites(): Flow<List<Cart>> {
        return flowOf(
            listOf(
                Cart(
                    Product(
                        "p_1",
                        "g_1",
                        "c_1",
                        "Product 1",
                        90.0,
                        Status.COMPLETE,
                        "Description 1",
                        listOf(R.drawable.ic_product),
                        emptyList(),
                        emptyList()
                    ),
                    ProductSize("Small", 9.0),
                    1
                ),
                Cart(
                    Product(
                        "p_2",
                        "g_2",
                        "c_2",
                        "Product 2",
                        90.0,
                        Status.COMPLETE,
                        "Description 1",
                        listOf(R.drawable.ic_product),
                        emptyList(),
                        emptyList()
                    ),
                    ProductSize("Small", 9.0),
                    1
                ),
                Cart(
                    Product(
                        "p_3",
                        "g_3",
                        "c_3",
                        "Product 3",
                        90.0,
                        Status.COMPLETE,
                        "Description 1",
                        listOf(R.drawable.ic_product),
                        emptyList(),
                        emptyList()
                    ),
                    ProductSize("Small", 9.0),
                    1
                ),
            )
        )
    }
}