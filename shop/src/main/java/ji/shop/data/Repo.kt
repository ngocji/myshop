package ji.shop.data

import ji.shop.R
import kotlinx.coroutines.Dispatchers
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

    suspend fun getProductsByCollection(collectionId: String, groupId: String) = withContext(Dispatchers.IO) {
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
                add(Group("g_$it", "c_$it", "Group $it"))
            }
        }
    }
}