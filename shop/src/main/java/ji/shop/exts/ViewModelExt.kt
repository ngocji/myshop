package ji.shop.exts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ji.shop.data.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun ViewModel.runCoroutine(context: CoroutineContext = Dispatchers.IO, block: suspend () -> Unit) {
    viewModelScope.launch(context = context) {
        block()
    }
}

fun <T> ViewModel.safeResultFlow(block: suspend () -> T): Flow<ResultWrapper<T>> =
    flow {
        emit(ResultWrapper.Loading)
        val data = block()
        emit(ResultWrapper.Success(data))
    }
        .catch {
            emit(ResultWrapper.Failure(it))
        }

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<ResultWrapper<T>>.mapWhenSuccess(block: (T) -> R) = this.mapLatest { result ->
    when (result) {
        is ResultWrapper.Success<T> -> ResultWrapper.Success(block(result.data))
        is ResultWrapper.Loading -> ResultWrapper.Loading
        is ResultWrapper.Failure -> ResultWrapper.Failure(result.error)
        else -> ResultWrapper.None
    }
}