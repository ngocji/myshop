package ji.shop

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED

class ShopViewModel : ViewModel() {
    val gotoFragmentEvent = Channel<() -> Fragment>(capacity = BUFFERED)
    val backEvent = Channel<Unit>()

    fun goto(fragment: () -> Fragment) {
        gotoFragmentEvent.trySend(fragment)
    }

    fun back() {
        backEvent.trySend(Unit)
    }
}