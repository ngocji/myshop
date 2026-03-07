package ji.shop.exts

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


fun <T> AppCompatActivity.collect(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    channel: Channel<T>,
    action: (t: T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            for (item in channel) {
                action(item)
            }
        }
    }
}

fun <T> AppCompatActivity.collect(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    flow: Flow<T>,
    action: (t: T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collect {
                action(it)
            }
        }
    }
}

fun <T> Fragment.collect(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    channel: Channel<T>,
    action: (t: T) -> Unit
) {
    viewLifecycleOwner.run {
        lifecycleScope.launch {
            repeatOnLifecycle(state) {
                for (item in channel) {
                    action(item)
                }
            }
        }
    }
}

fun <T> Fragment.collect(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    flow: Flow<T>,
    action: (t: T) -> Unit
) {
    viewLifecycleOwner.run {
        lifecycleScope.launch {
            repeatOnLifecycle(state) {
                flow.collect { action(it) }
            }
        }
    }
}