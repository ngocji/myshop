package ji.shop.data.domain

data class WrapUpdateData<T>(
    val data: T,
    val timeStamp: Long = System.currentTimeMillis()
)