package ji.shop.data

data class WrapUpdateData<T>(
    val data: T,
    val timeStamp: Long = System.currentTimeMillis()
)