package ji.shop.data

import ji.shop.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object Repo {
    suspend fun getShopCategory() = withContext(Dispatchers.IO) {
        buildList {
            add(
                ShopCategory(
                    id = "shop_festival",
                    name = "Festival Concessions"
                )
            )
        }
    }

    suspend fun getCollections() = withContext(Dispatchers.IO) {
        delay(2000)
        buildList {
            repeat(10) {
                add(
                    Collection(
                        id = "c_$it",
                        name = "Collections $it",
                        image = listOf(R.drawable.ic_colection)
                    )
                )
            }
        }
    }

    suspend fun getProductsByCollection(collectionId: String, groupId: String) =
        withContext(Dispatchers.IO) {
            buildList {
                repeat(10) {
                    add(
                        Product(
                            id = "p_$it",
                            groupId = "g_$it",
                            collectionId = collectionId,
                            name = "Product $it",
                            price = 90.0,
                            description = "Description $it",
                            images = listOf(R.drawable.ic_product),
                            status = Status.COMPLETE,
                            sizes = listOf(
                                ProductSize("Small", 9.0),
                                ProductSize("Medium", 10.0),
                                ProductSize("Large", 12.0),
                                ProductSize("Extra Large", 14.0),
                            ),
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
                        )
                    )
                }
            }
        }

    suspend fun getGroups(collectionId: String) = withContext(Dispatchers.IO) {
        buildList {
            repeat(4) {
                add(Group("g_$it", collectionId, "Group $it"))
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
                                images = listOf(R.drawable.ic_inventory, R.drawable.ic_inventory, R.drawable.ic_inventory,
                                    R.drawable.ic_inventory),
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
            image=  R.drawable.ic_card,
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