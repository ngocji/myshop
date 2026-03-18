package ji.shop.data.domain

data class CheckoutResult(
    val isSuccess: Boolean,
    val message: String? = null
)