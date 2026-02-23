package ji.shop

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ji.shop.data.Cart
import ji.shop.data.Collection
import ji.shop.data.Group
import ji.shop.data.Product
import ji.shop.data.Repo
import ji.shop.data.ResultWrapper
import ji.shop.data.TabType
import ji.shop.data.WrapUpdateData
import ji.shop.exts.mapWhenSuccess
import ji.shop.exts.safeFlow
import ji.shop.exts.safeResultFlow
import ji.shop.fragments.ShoppingFragment
import ji.shop.items.CollectionGridItemUi
import ji.shop.items.CollectionLinearItemUi
import ji.shop.items.GroupItemUi
import ji.shop.items.ProductItemUi
import ji.shop.utils.NumberFormater
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class ShopViewModel(context: Application) : AndroidViewModel(context) {
    val gotoFragmentEvent = Channel<() -> Fragment>(capacity = BUFFERED)
    val backEvent = Channel<Unit>()

    // state for user
    val myBalanceState = MutableStateFlow(0.0)
    val cartsState = MutableStateFlow(WrapUpdateData<Set<Cart>>(emptySet()))
    val isNfcEnabledState = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val cartPriceState = cartsState.mapLatest { wrap ->
        wrap.data.sumOf { it.getTotalPrice() }.takeIf { it > 0 }
            ?.let { NumberFormater.formatNumberLocale(it) }
    }


    // trigger refresh
    private val triggerRefreshCollectionsFlow = MutableSharedFlow<Unit>(replay = 1)


    // state for shop
    val tabTabTypeState = MutableStateFlow(TabType.Inventory)

    val shopCategoriesFlow = safeFlow {
        Repo.getShopCategory() to 0
    }.shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    val collectionsFlow = triggerRefreshCollectionsFlow.onStart { emit(Unit) }
        .flatMapLatest { safeResultFlow { Repo.getCollections() } }.mapWhenSuccess { items ->
            items.map {
                CollectionGridItemUi(it)
            } to items.map {
                CollectionLinearItemUi(it)
            }
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

    val collectionState = MutableStateFlow<Collection?>(null)

    val groupsFlow =
        collectionState.filterNotNull().flatMapLatest { safeFlow { Repo.getGroups(it.id) } }
            .filterNotNull().mapLatest { items ->
                groupState.tryEmit(items.firstOrNull())
                items.map {
                    GroupItemUi(it)
                }
            }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

    val groupState = MutableStateFlow<Group?>(null)
    val groupSelectedIndexFlow = combine(groupsFlow, groupState) { groups, group ->
        group?.let { groups.indexOfFirst { it.data.id == group.id } } ?: -1
    }

    val productsFlow = groupState
        .filterNotNull()
        .flatMapLatest {
            safeResultFlow {
                Repo.getProductsByCollection(
                    it.collectionId, it.id
                )
            }
        }
        .mapWhenSuccess { items ->
            items.map {
                ProductItemUi(
                    it,
                    count = getProductCountOfCart(it),
                    isUseToggleCount = isNfcEnabled()
                )
            }
        }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)


    val productCountNotifyFlow = combine(cartsState, productsFlow) { carts, products ->
        if (products is ResultWrapper.Success) {
            val items = products.data
            val index = carts.data.mapNotNull { cart ->
                val index = items.indexOfFirst { it.data.id == cart.product.id }
                val item = items.getOrNull(index)
                if (item != null) {
                    item.count = cart.count
                    index
                } else {
                    null
                }
            }
            index.minOrNull() to index.maxOrNull()
        } else {
            null
        }
    }
        .filterNotNull()

    init {
        goto {
            ShoppingFragment()
        }
    }

    fun goto(fragment: () -> Fragment) {
        gotoFragmentEvent.trySend(fragment)
    }

    fun back() {
        backEvent.trySend(Unit)
    }

    fun changeTabType(newTabType: TabType) {
        tabTabTypeState.value = newTabType
    }

    fun refreshCollectionsFlow() {
        triggerRefreshCollectionsFlow.tryEmit(Unit)
    }

    fun isViewTheSameCollection(collection: Collection): Boolean {
        return collectionState.value == collection
    }

    fun setViewCollection(collection: Collection?) {
        collectionState.tryEmit(collection)
    }

    fun setViewGroup(group: Group?) {
        groupState.tryEmit(group)
    }

    private fun getProductCountOfCart(product: Product): Int {
        return cartsState.value.data.find { it.product.id == product.id }?.count ?: 0
    }

    fun updateProductCountOfCart(product: Product, count: Int) {
        cartsState.update {
            val carts = it.data.toMutableSet()
            val extProduct = carts.find { c -> c.product.id == product.id }
            if (extProduct != null) {
                carts.remove(extProduct)
            }

            if (count > 0) {
                carts.add(
                    extProduct?.copy(count = count) ?: Cart(
                        product = product,
                        size = product.sizes.firstOrNull(),
                        count = count,
                        additional = emptyMap()
                    )
                )
            }

            WrapUpdateData(data = carts)
        }
    }

    fun setNfcEnabled(enabled: Boolean) {
        isNfcEnabledState.tryEmit(enabled)
    }

    fun isNfcEnabled() = isNfcEnabledState.value

    fun getCart(product: Product): Cart? {
        return cartsState.value.data.find { it.product.id == product.id }
    }

    fun addToCart(cart: Cart) {
        cartsState.update {
            val carts = it.data.toMutableSet()
            val extProduct = carts.find { c -> c.product.id == cart.product.id }
            if (extProduct != null) {
                carts.remove(extProduct)
            }

            carts.add(cart)
            WrapUpdateData(data = carts)
        }
    }

    fun getCartItems(): List<Cart> {
        return cartsState.value.data.toList()
    }

    fun updateCarts(carts: List<Cart>) {
        cartsState.update {
            WrapUpdateData(data = carts.toSet())
        }
    }
}