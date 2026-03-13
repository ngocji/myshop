package ji.shop.data.domain

sealed class ResultWrapper<out T> {
    class Success<T>(val data: T) : ResultWrapper<T>()
    class Failure(val error: Throwable) : ResultWrapper<Nothing>()

    object Empty : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
    object None : ResultWrapper<Nothing>()
}