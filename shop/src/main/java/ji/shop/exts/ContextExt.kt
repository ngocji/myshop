package ji.shop.exts

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import ji.shop.R

fun Context?.isTablet() = this?.resources?.getBoolean(R.bool.isTablet)
    ?: Resources.getSystem().displayMetrics.let { displayMetrics ->
        val widthDp = displayMetrics.widthPixels / displayMetrics.density
        val heightDp = displayMetrics.heightPixels / displayMetrics.density
        val smallestWidthDp = minOf(widthDp, heightDp)
        return smallestWidthDp >= 600
    }

fun Context.width() = resources.displayMetrics.widthPixels

fun Context.height(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wm = getSystemService(WindowManager::class.java)
        val metrics = wm.currentWindowMetrics
        val insets = metrics.windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.systemBars()
        )
        metrics.bounds.height() - insets.top - insets.bottom
    } else {
        val dm = resources.displayMetrics
        val full = dm.heightPixels

        val status = resources.getIdentifier("status_bar_height", "dimen", "android")
            .takeIf { it > 0 }?.let(resources::getDimensionPixelSize) ?: 0

        val nav = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            .takeIf { it > 0 }?.let(resources::getDimensionPixelSize) ?: 0

        full - status - nav
    }
}