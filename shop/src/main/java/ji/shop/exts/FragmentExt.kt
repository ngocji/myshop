@file:JvmName("FragmentExt")

package ji.shop.exts

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.jvm.javaClass
import kotlin.let


@Suppress("UNCHECKED_CAST")
fun <F : Fragment> AppCompatActivity.getFragment(fragmentClass: Class<F>): F? {
    val navHostFragment = this.supportFragmentManager.fragments.first() as NavHostFragment

    navHostFragment.childFragmentManager.fragments.forEach {
        if (fragmentClass.isAssignableFrom(it.javaClass)) {
            return it as F
        }
    }

    return null
}

fun <T> Fragment.getNavigationResult(
    key: String = "result",
    destinationId: Int? = null
): LiveData<T>? {
    val backStack = destinationId?.let { findNavController().getBackStackEntry(it) }
        ?: findNavController().currentBackStackEntry

    return backStack?.savedStateHandle?.getLiveData(key)
}

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.setNavigationResult(result: T, destinationId: Int, key: String = "result") {
    try {
        findNavController().getBackStackEntry(destinationId).savedStateHandle.set(key, result)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



fun FragmentActivity.onBackPressedOverride(func: () -> Unit) {
    this.onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                func()
            }
        })
}

fun Fragment.onBackPressedOverride(func: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                func.invoke()
            }
        })
}

fun Fragment.observeOnDestroy(action: () -> Unit) {
    viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) { viewLifecycleOwner ->
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                action.invoke()
            }
        })
    }
}

fun Fragment.observeOnResume(action: () -> Unit) {
    viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) { viewLifecycleOwner ->
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                action.invoke()
            }
        })
    }
}

fun Fragment.observeOnPause(action: () -> Unit) {
    viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) { viewLifecycleOwner ->
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                action.invoke()
            }
        })
    }
}


// region support for java
fun FragmentActivity.onBackPressOverride(func: Runnable) {
    onBackPressedOverride {
        func.run()
    }
}

fun Fragment.onBackPressOverride(func: Runnable) {
    onBackPressedOverride {
        func.run()
    }
}

fun Fragment.observeOnDestroy(runnable: Runnable) {
    observeOnDestroy { runnable.run() }
}

fun Fragment.observeOnResume(runnable: Runnable) {
    observeOnResume { runnable.run() }
}

fun Fragment.observeOnPause(runnable: Runnable) {
    observeOnPause { runnable.run() }
}