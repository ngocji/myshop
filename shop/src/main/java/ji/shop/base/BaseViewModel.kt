package ji.shop.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ji.shop.data.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val repo by lazy { Repo }

    fun safeCall(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        block()
    }
}